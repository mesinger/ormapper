package mesi.employee

import mesi.orm.conn.DatabaseSystem
import mesi.orm.persistence.BaseRepository
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.annotations.PersistentEnum
import mesi.orm.persistence.annotations.PersistentTransient
import mesi.orm.persistence.annotations.Primary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Persistent
data class Employee (
        @Primary val id : Long,
        val firstName : String,
        val lastName : String,
        @PersistentEnum val gender : Gender,
        val isChef : Boolean,
        val salary : Float,
        val entryDate : LocalDate,
        val shiftStart : LocalTime,
        @PersistentTransient val projectDeadLine : LocalDateTime
)

enum class Gender {
    MALE,
    FEMALE
}

fun main(args: Array<String>) {

    val mesi = Employee(1, "mesi", "inger", Gender.MALE, true, 700.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0), LocalDateTime.of(2020, 6, 1, 12, 0, 0))
    val rico = Employee(2, "rico", "pc", Gender.FEMALE, false, 25f, LocalDate.of(2020, 4, 1), LocalTime.of(8, 30, 0), LocalDateTime.of(2020, 6, 1, 12, 0, 0))
//    val po = PersistentObject.from(mesi)
    val employeeRepo = BaseRepository.create<Long, Employee>(DatabaseSystem.SQLITE, "jdbc:sqlite:employees.db");

//    employeeRepo.save(mesi)
//    employeeRepo.save(rico)

    val fetched = employeeRepo.get(1)
}
