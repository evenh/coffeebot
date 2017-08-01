package net.evenh.coffeebot

import me.ramswaroop.jbot.core.slack.models.Attachment
import me.ramswaroop.jbot.core.slack.models.Field
import me.ramswaroop.jbot.core.slack.models.RichMessage
import net.evenh.coffeebot.config.AppConfig
import net.evenh.coffeebot.machine.Machine
import net.evenh.coffeebot.machine.MachineService
import net.evenh.coffeebot.machine.MachineStatus
import org.ocpsoft.prettytime.PrettyTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.*

@RestController
class SlashCommandController @Autowired constructor(val config: AppConfig, val service: MachineService) {
    private val log = LoggerFactory.getLogger(SlashCommandController::class.java)

    @PostMapping(value = "/handle", consumes = arrayOf(APPLICATION_FORM_URLENCODED_VALUE))
    fun onReceiveSlashCommand(@RequestParam("token") token: String,
                              @RequestParam("channel_name") channelName: String,
                              @RequestParam("user_name") userName: String,
                              @RequestParam("command") command: String,
                              @RequestParam("text") text: String): RichMessage {

        log.info("'{}' requested a status change in '{}'", userName, channelName)

        // Check if token is valid
        if (!validateToken(token)) {
            return RichMessage("You're not allowed to use the mighty powers of the coffeebot.")
        }

        // If empty, show overview
        if (text.trim().isNullOrEmpty()) {
            return createStatusOverview()
        }

        // Split status and machine name
        val splitted = text.split(delimiters = " ")
        val machine = splitted.first()
        val status = splitted.last()

        // See if we can determine a new status from user input
        val newStatus = MachineStatus.fromReadableStatus(status)

        if (!newStatus.isPresent) {
            return message("Invalid status supplied").encodedMessage()
        }

        val res: RichMessage

        if (service.updateStatus(machine, newStatus.get())) {
            res = message("Successfully marked " + machine + " as " + newStatus.get().readableStatus)
        } else {
            res = message("Could not mark " + machine + " as " + newStatus.get().readableStatus)
        }

        return res.encodedMessage()
    }

    /**
     * Validate a received token against the one stored in app config.
     */
    private fun validateToken(receivedToken: String): Boolean {
        return receivedToken == config.slashCommandToken
    }

    private fun message(text: String): RichMessage {
        val richMessage = RichMessage(text)
        richMessage.responseType = "in_channel"

        return richMessage
    }

    private fun createStatusOverview(): RichMessage {
        val msg = message("")

        msg.attachments = service.machines
                .map { convertToAttachment(it) }
                .toTypedArray()

        return msg.encodedMessage()
    }

    private fun convertToAttachment(machine: Machine): Attachment {
        val att = Attachment()
        val lastChange = machine.statusChanges.last()


        val nameField = Field()
        nameField.title = "Name"
        nameField.value = machine.name
        nameField.isShortEnough = true

        val statusField = Field()
        statusField.title = "Status"
        statusField.value = lastChange.status.readableStatus
        statusField.isShortEnough = true

        val lastChangedField = Field()
        lastChangedField.title = "Last updated"
        lastChangedField.value = timeAgo(lastChange.timestamp)
        lastChangedField.isShortEnough = true

        // Put fields into attachment
        att.fields = arrayOf(nameField, statusField, lastChangedField)
        // Set color according to status
        att.color = lastChange.status.color

        return att
    }

    private fun timeAgo(time: ZonedDateTime): String {
        val converted = Date.from(time.toInstant())
        val pt = PrettyTime(Date())

        return pt.format(converted)
    }
}
