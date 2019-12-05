package mesi.orm.transaction

internal interface TransactionState {
    fun addTask(task : () -> Unit)
}

internal class NoTransactionState : TransactionState {
    override fun addTask(task: () -> Unit) {
        task()
    }
}

internal class CurrentTransactionState(private val transaction: BaseRepositoryTransaction) : TransactionState {
    override fun addTask(task: () -> Unit) {
        transaction.addTask(task)
    }
}
