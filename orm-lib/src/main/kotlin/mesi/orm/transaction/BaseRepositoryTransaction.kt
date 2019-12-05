package mesi.orm.transaction

import mesi.orm.exception.ORMesiException

internal class BaseRepositoryTransaction : RepositoryTransaction {

    private var hasBegun = false
    val executingTasks = mutableListOf<() -> Unit>()

    fun addTask(task : () -> Unit) {
        executingTasks.add(task)
    }

    override fun begin() {
        hasBegun = true
    }

    override fun commit() {
        require(hasBegun) {throw ORMesiException("No prior call of RepositoryTransaction::begin")}
        executingTasks.forEach { task -> task() }
    }

    override fun rollback() {
        require(hasBegun) {throw ORMesiException("No prior call of RepositoryTransaction::begin")}
        executingTasks.clear()
    }
}