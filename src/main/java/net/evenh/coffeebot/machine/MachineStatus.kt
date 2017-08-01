package net.evenh.coffeebot.machine

import java.util.*

enum class MachineStatus(val readableStatus: String, val color: String) {
    UP("Up", "good"),
    DOWN("Down", "danger"),
    UNSTABLE("Unstable", "warning");

    companion object {
        /**
         * Reverse lookup.
         */
        fun fromReadableStatus(text: String): Optional<MachineStatus> {
            return values()
                    .firstOrNull { it.readableStatus == text.capitalize() }
                    ?.let { Optional.of(it) }
                    ?: Optional.empty()
        }
    }
}
