package mesi.orm.util

import mesi.orm.persistence.annotations.*

data class NotPersistentClass (
        @Primary val id : Long
)

@Persistent
open class PersistentClass (
        @Primary val id : Long
)

@Persistent
class SubPersistentClass(id : Long, val name : String) : PersistentClass(id)

@Persistent
data class WithPrimary (
        @Primary val id : Long
)

@Persistent
data class WithoutPrimary (
        val id : Long
)

@Persistent
data class WithEnums (
        @PersistentEnum val enum : Enum
)

enum class Enum {
    First, Second
}

@Persistent
data class WithForeigns (
        @Primary val id : Long,
        @Foreign(relation = ForeignRelation.ONE_TO_ONE) val foreign : PersistentClass,
        @Foreign(relation = ForeignRelation.MANY_TO_ONE) val foreign2 : PersistentClass,
        @Foreign(relation = ForeignRelation.ONE_TO_MANY, clazz = PersistentClass::class) val foreign3 : List<PersistentClass>
)

@Persistent
data class WithoutForeigns (
        val foreign : PersistentClass
)