package net.evenh.coffeebot;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.google.common.collect.Iterables;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;
import me.ramswaroop.jbot.core.slack.models.Attachment;
import me.ramswaroop.jbot.core.slack.models.Field;
import me.ramswaroop.jbot.core.slack.models.RichMessage;
import net.evenh.coffeebot.config.AppConfig;
import net.evenh.coffeebot.machine.Machine;
import net.evenh.coffeebot.machine.MachineService;
import net.evenh.coffeebot.machine.MachineStatus;
import net.evenh.coffeebot.machine.StatusChange;
import org.ocpsoft.prettytime.PrettyTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlashCommandController {

  private static final Logger logger = LoggerFactory.getLogger(SlashCommandController.class);

  @Autowired
  AppConfig config;

  @Autowired
  MachineService service;

  @PostMapping(value = "/handle", consumes = APPLICATION_FORM_URLENCODED_VALUE)
  public RichMessage onReceiveSlashCommand(@RequestParam("token") String token,
      @RequestParam("team_id") String teamId,
      @RequestParam("team_domain") String teamDomain,
      @RequestParam("channel_id") String channelId,
      @RequestParam("channel_name") String channelName,
      @RequestParam("user_id") String userId,
      @RequestParam("user_name") String userName,
      @RequestParam("command") String command,
      @RequestParam("text") String text,
      @RequestParam("response_url") String responseUrl) {

    logger.info("'{}' requested a status change in '{}'", userName, channelName);

    // Check if token is valid
    if (!validateToken(token)) {
      return new RichMessage("You're not allowed to use the mighty powers of the coffeebot.");
    }

    // If empty, show overview
    if (text.trim().isEmpty()) {
      return createStatusOverview();
    }

    // Split status and machine name
    String[] splitted = text.split(" ");
    String status = splitted[0];
    String machine = splitted[1];

    final Optional<MachineStatus> newStatus = MachineStatus.fromReadableStatus(status);

    if (!newStatus.isPresent()) {
      return message("Invalid status supplied").encodedMessage();
    }

    RichMessage res;

    if (service.updateStatus(machine, newStatus.get())) {
      res = message("Successfully marked " + machine + " as " + newStatus.get().readableStatus);
    } else {
      res = message("Could not mark " + machine + " as " + newStatus.get().readableStatus);
    }

    return res.encodedMessage();
  }

  /**
   * Validate a received token against the one stored in app config.
   */
  private boolean validateToken(final String receivedToken) {
    return receivedToken.equals(config.getSlashCommandToken());
  }

  private RichMessage message(final String text) {
    RichMessage richMessage = new RichMessage(text);
    richMessage.setResponseType("in_channel");

    return richMessage;
  }

  private RichMessage createStatusOverview() {
    RichMessage msg = message("");

    Attachment[] attachments = service.getMachines()
        .stream()
        .map(this::convertToAttachment)
        .toArray(Attachment[]::new);

    msg.setAttachments(attachments);

    return msg.encodedMessage();
  }

  private Attachment convertToAttachment(Machine machine) {
    final Attachment att = new Attachment();
    final StatusChange lastChange = Iterables.getLast(machine.getStatusChanges());

    final Field nameField = new Field();
    nameField.setTitle("Name");
    nameField.setValue(machine.getName());
    nameField.setShortEnough(true);

    final Field statusField = new Field();
    statusField.setTitle("Status");
    statusField.setValue(lastChange.getStatus().readableStatus);
    statusField.setShortEnough(true);

    final Field lastChangedField = new Field();
    lastChangedField.setTitle("Last updated");
    lastChangedField.setValue(timeAgo(lastChange.getTimestamp()));
    lastChangedField.setShortEnough(true);

    att.setFields(new Field[]{nameField, statusField, lastChangedField});
    att.setColor(lastChange.getStatus().color);

    return att;
  }

  private String timeAgo(ZonedDateTime time) {
    Date converted = Date.from(time.toInstant());
    PrettyTime pt = new PrettyTime(new Date());

    return pt.format(converted);
  }
}
