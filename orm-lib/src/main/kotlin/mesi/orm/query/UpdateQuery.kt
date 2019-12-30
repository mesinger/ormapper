package mesi.orm.query

import mesi.orm.persistence.transform.PersistentObject

internal abstract class UpdateQuery(protected val persistentObject: PersistentObject) : Query()