package mesi.orm.transaction

import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.Repository
import java.lang.Exception
import java.lang.RuntimeException

/**
 * Transaction object for a [mesi.orm.persistence.Repository]
 */
interface RepositoryTransaction {

    /**
     * begins the transaction
     */
    fun begin() : Unit

    /**
     * commits the transaction, used when all repository operations should be executed
     */
    fun commit() : Unit

    /**
     * rollbacks the transaction, used if all non-commited operations should be thrown away
     */
    fun rollback() : Unit
}

fun transactional(repos : List<Repository<*, *>>, block : () -> Unit) {

    val transactions = repos.map { it.getTransaction() }
    transactions.forEach(RepositoryTransaction::begin)

    try {
        block()
        transactions.forEach(RepositoryTransaction::commit)
    } catch (ex : Exception) {
        println(ex.message)
        transactions.forEach(RepositoryTransaction::rollback)
    }
}