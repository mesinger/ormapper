package mesi.employee

import mesi.orm.conn.DatabaseSystem
import mesi.orm.persistence.BaseRepository
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.annotations.PersistentTransient
import mesi.orm.persistence.annotations.Primary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Persistent
data class Employee (
        @Primary var id : Long,
        var firstName : String,
        var lastName : String,
        @PersistentTransient var gender : Gender,
        var isChef : Boolean,
        var salary : Float,
        var entryDate : LocalDate,
        var shiftStart : LocalTime,
        @PersistentTransient var projectDeadLine : LocalDateTime
) {
    constructor() : this(0, "", "", Gender.FEMALE, false, 0f, LocalDate.MIN, LocalTime.MIDNIGHT, LocalDateTime.MIN)
}

enum class Gender {
    MALE,
    FEMALE
}

fun main(args: Array<String>) {
    val mesi = Employee(1, "mesi", "inger", Gender.MALE, true, 700.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0), LocalDateTime.of(2020, 6, 1, 12, 0, 0))
    val rico = Employee(2, "rico", "pc", Gender.FEMALE, false, 25f, LocalDate.of(2020, 4, 1), LocalTime.of(8, 30, 0), LocalDateTime.of(2020, 6, 1, 12, 0, 0))

    val employeeRepo = BaseRepository.Factory.create<Long, Employee>(DatabaseSystem.SQLITE, "jdbc:sqlite:employees.db");

    employeeRepo.save(mesi)
    employeeRepo.save(rico)

//    val fetched = employeeRepo.get(1)
}
