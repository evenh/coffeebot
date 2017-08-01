package net.evenh.coffeebot.machine;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
  Optional<Machine> findOneByNameIgnoringCase(String name);
}
