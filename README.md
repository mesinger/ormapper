# lazy ormapper framework for kotlin
Do not use this framework for production code

## Build
```sh
gradle clean build
```

When using this framework in your application, you need a implementation of slf4j. If you just want to print to stdout, you can add this dependency to your application.
```groovy
implementation group: 'org.slf4j', name: 'slf4j-simple', version: '1.7.30'
```

## Documentation
```sh
gradle dokka
```

## Usage
```kotlin
@Persistent
@Table(tableName = "departments_table")
data class Department (
        @Primary val name : String
)

//...

val management = Department("management")

persistenceContext(DatabaseSystem.SQLITE, "jdbc:sqlite:employees.db") {
        val departmentRepo = createRepo<String, Department>()

        transactional {
            departmentRepo.save(management)
        }
}
```
## Annotations
```kotlin
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
        @PersistentTransient val internalTimer : LocalTime
)
```
