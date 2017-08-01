package net.evenh.coffeebot.machine

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MachineService @Autowired constructor(val repository: MachineRepository) {
    private val log = LoggerFactory.getLogger(MachineService::class.java)

    val machines: List<Machine>
        get() = repository.findAll()

    fun updateStatus(machineName: String, status: MachineStatus): Boolean {
        val machine = repository.findOneByNameIgnoringCase(machineName)

        if (!machine.isPresent) {
            log.warn("Machine named '{}' was not found", machineName)
            return false
        }

        val foundMachine = machine.get()

        foundMachine.appendStatusChange(StatusChange.newChange(status))

        repository.save(foundMachine)

        log.info("Updated {} as {}", foundMachine, status)

        return true
    }
}
