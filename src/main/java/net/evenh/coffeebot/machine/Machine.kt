package net.evenh.coffeebot.machine

import javax.persistence.CascadeType.ALL
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
data class Machine (@Id @GeneratedValue val id: Long? = null,
                    val name: String,
                    @OneToMany(cascade = arrayOf(ALL)) var statusChanges : List<StatusChange>){

    fun appendStatusChange(statusChange: StatusChange) {
        statusChanges += statusChange
    }

    override fun toString(): String {
        return "Machine(id=$id, name='$name')"
    }
}
