package net.evenh.coffeebot.machine;

import java.util.Optional;

public enum MachineStatus {
  UP("Up", "good"),
  DOWN("Down", "danger"),
  UNSTABLE("Unstable", "warning");

  public final String readableStatus;
  public final String color;

  MachineStatus(String readableStatus, String color) {
    this.readableStatus = readableStatus;
    this.color = color;
  }

  /**
   * Reverse lookup.
   */
  public static Optional<MachineStatus> fromReadableStatus(String text) {
    for (MachineStatus status : values()) {
      if (status.readableStatus.equalsIgnoreCase(text)) {
        return Optional.of(status);
      }
    }

    return Optional.empty();
  }
}
