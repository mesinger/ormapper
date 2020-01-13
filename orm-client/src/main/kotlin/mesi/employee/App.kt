package mesi.employee

import mesi.orm.conn.DatabaseSystem
import mesi.orm.persistence.annotations.*
import mesi.orm.persistence.context.persistenceContext
import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Persistent
class Person(
        @Primary val id : Long,
        val firstName : String,
        val lastName : String
)

@Persistent
class Student(
        id : Long,
        firstName : String,
        lastName : String,
        val uid : String
) : Person(id, firstName, lastName)

@Persistent
data class Employee (
        @Primary val id : Long,
        val firstName : String,
        val lastName : String,
        @PersistentEnum val gender : Gender,
        @Foreign(relation = ForeignRelation.MANY_TO_ONE) val department : Department,
        @Foreign(relation = ForeignRelation.ONE_TO_MANY, clazz = Project::class) val projects : List<Project>,
        val isChef : Boolean,
        val salary : Float,
        val entryDate : LocalDate,
        @PersistentTransient val shiftStart : LocalTime
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

@Persistent
data class Project (
        @Primary val id : Long,
        val name : String,
        val deadline : LocalDateTime
)

fun main(args: Array<String>) {

    val logger = KotlinLogging.logger("Application")

    val management = Department("management")
    val facility = Department("facility")
    val project1 = Project(1, "project 1", LocalDateTime.of(2020, 6, 1, 12, 0, 0))
    val project2 = Project(2, "project 2", LocalDateTime.of(2020, 6, 1, 12, 0, 0))
    val project3 = Project(3, "heusl putzn", LocalDateTime.of(2020, 6, 1, 12, 0, 0))

    val mesi = Employee(1, "mesi", "inger", Gender.MALE, management, listOf(project1, project2), true, 700.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0))
    val rico = Employee(2, "rico", "pc", Gender.FEMALE, management, listOf(), true, 700.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0))
    val nimmervoll = Employee(3, "nimmer", "voll", Gender.FEMALE, facility, listOf(project3), false, 700.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0))

    persistenceContext(DatabaseSystem.SQLITE, "jdbc:sqlite:employees.db") {
        val departmentRepo = createRepo<String, Department>()
        val projectRepo = createRepo<Long, Project>()
        val employeeRepo = createRepo<Long, Employee>()

//        val studentRepo = createRepo<Long, Student>()
//        studentRepo.save(Student(1, "rico", "pc", "uid"))
//        val student = studentRepo.get(1)

        departmentRepo.save(management)
        departmentRepo.save(facility)
        projectRepo.save(project1)
        projectRepo.save(project2)
        projectRepo.save(project3)
        employeeRepo.save(mesi)
        employeeRepo.save(rico)
        employeeRepo.save(nimmervoll)

        employeeRepo.update(Employee(1, "mesi", "inger", Gender.MALE, management, listOf(project1, project2), true, 80000.99f, LocalDate.of(2019, 11, 21), LocalTime.of(10, 0, 0)))

        val fetchedMesi = employeeRepo.get(1)
        val fetchedMesi2 = employeeRepo.getAll().where("department='management'").and().where("id=1").fetch()

        logger.info { "Fetched Employee = $fetchedMesi" }
    }
}
