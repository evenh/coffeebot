package net.evenh.coffeebot.machine

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
data class StatusChange(@Id @GeneratedValue val id: Long? = null,
                        val timestamp: ZonedDateTime,
                        @Enumerated(EnumType.STRING) val status: MachineStatus) {
    companion object {
        fun newChange(status: MachineStatus) = StatusChange(null, ZonedDateTime.now(), status)
    }
}
