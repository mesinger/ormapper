package mesi.employee

import mesi.orm.conn.DatabaseSystem
import mesi.orm.persistence.BaseRepository
import mesi.orm.persistence.annotations.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Persistent
data class Employee (
        @Primary val id : Long,
        val firstName : String,
        val lastName : String,
        @PersistentEnum val gender : Gender,
        @Foreign(relation = ForeignRelation.MANY_TO_ONE) val department : Department,
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

@Persistent
@Table(tableName = "departments_table")
data class Department (
        @Primary val name : String
)

fun main(args: Array<String>) {

    val management = Department("management")
    val mesi = Employee(1, "mesi", "inger", Gender.MALE, management,true, 700.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0), LocalDateTime.of(2020, 6, 1, 12, 0, 0))
    val rico = Employee(2, "rico", "pc", Gender.MALE, management,false, 500.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0), LocalDateTime.of(2020, 6, 1, 12, 0, 0))

    val departmentRepo = BaseRepository.create<String, Department>(DatabaseSystem.SQLITE, "jdbc:sqlite:employees.db");
    val employeeRepo = BaseRepository.create<Long, Employee>(DatabaseSystem.SQLITE, "jdbc:sqlite:employees.db");

//    departmentRepo.save(management)
//    employeeRepo.save(mesi)
//    employeeRepo.save(rico)

    val allEmployees = employeeRepo.getAll().fetch()
    val managementEmployees = employeeRepo.getAll().where("department='management'").fetch()
    val d = employeeRepo.getAll().where("id=1").or().where("id=2").and().where("firstName='mesi'").fetch()
}
