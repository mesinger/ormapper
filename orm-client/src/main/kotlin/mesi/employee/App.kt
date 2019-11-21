package mesi.employee

import mesi.orm.conn.DatabaseSystem
import mesi.orm.persistence.BaseRepository
import mesi.orm.persistence.Persistent
import mesi.orm.persistence.Primary

@Persistent
data class Employee (
        @Primary val id : Long,
        val name : String
)

fun main(args: Array<String>) {
    val employee = Employee(1, "mesi")
    val employeeRepo = BaseRepository.Factory.create<Long, Employee>(DatabaseSystem.SQLITE, "jdbc:sqlite:employees.db");
    employeeRepo.save(employee)
}
