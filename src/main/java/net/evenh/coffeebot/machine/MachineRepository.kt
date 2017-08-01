package net.evenh.coffeebot.machine

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MachineRepository : JpaRepository<Machine, Long> {
    fun findOneByNameIgnoringCase(name: String): Optional<Machine>
}
