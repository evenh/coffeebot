package net.evenh.coffeebot.machine;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "machine")
public class Machine {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  @OneToMany(cascade = ALL)
  private List<StatusChange> statusChanges = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<StatusChange> getStatusChanges() {
    return statusChanges;
  }

  public void appendStatusChange(StatusChange statusChange) {
    this.statusChanges.add(statusChange);
  }

  @Override
  public String toString() {
    return "Machine{"
        + "id=" + id
        + ", name='" + name + '\''
        + '}';
  }
}
