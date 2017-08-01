package net.evenh.coffeebot.machine;

import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StatusChange {
  @Id
  @GeneratedValue
  private Long id;

  private ZonedDateTime timestamp;

  @Enumerated(EnumType.STRING)
  private MachineStatus status;

  private StatusChange() {}

  private StatusChange(MachineStatus status) {
    this.status = Objects.requireNonNull(status);
    this.timestamp = ZonedDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public MachineStatus getStatus() {
    return status;
  }

  public static StatusChange newChange(MachineStatus status) {
    return new StatusChange(status);
  }
}
