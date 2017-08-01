package net.evenh.coffeebot.machine;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachineService {
  private static final Logger log = LoggerFactory.getLogger(MachineService.class);

  @Autowired
  MachineRepository repository;

  public List<Machine> getMachines() {
    return repository.findAll();
  }

  public boolean updateStatus(String machineName, MachineStatus status) {
    final Optional<Machine> machine = repository.findOneByNameIgnoringCase(machineName);

    if (!machine.isPresent()) {
      log.warn("Machine named '{}' was not found", machineName);
      return false;
    }

    final Machine foundMachine = machine.get();
    foundMachine.appendStatusChange(StatusChange.newChange(status));

    repository.save(foundMachine);

    log.info("Updated {} as {}", foundMachine, status);

    return true;
  }
}
