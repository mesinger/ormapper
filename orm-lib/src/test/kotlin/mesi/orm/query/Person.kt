package mesi.orm.query

import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.annotations.Primary

@Persistent
data class Person (
    @Primary val id: Long,
    val name: String,
    val surname: String
)