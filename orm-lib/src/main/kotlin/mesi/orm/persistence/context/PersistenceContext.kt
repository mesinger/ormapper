package mesi.orm.persistence.context

import mesi.orm.cache.RepositoryCache
import mesi.orm.conn.DatabaseConnectionFactory
import mesi.orm.conn.DatabaseSystem
import mesi.orm.exception.ORMesiException
import mesi.orm.persistence.BaseRepository
import mesi.orm.persistence.Repository
import mesi.orm.persistence.annotations.Persistent
import mesi.orm.persistence.fetch.ResultSetParser
import mesi.orm.query.QueryBuilderFactory
import mesi.orm.transaction.RepositoryTransaction
import mesi.orm.util.Persistence

/**
 * context for persistent operations with the framework
 * this class has to be used with the [persistenceContext] type safe builder
 */
class PersistenceContext internal constructor(
        val system : DatabaseSystem,
        val connectionString: String
) {

    val createdRepositories = mutableListOf<Repository<*, *>>()

    /**
     * creates implementations of [Repository] with ids of type [PRIMARY], and objects of type [ENTITY]
     * connects to a database system of type [system] with given [connectionString]
     */
    inline fun <reified PRIMARY : Any, reified ENTITY : Any> createRepo() : Repository<PRIMARY, ENTITY> {

        // checks for no-arg constructor
        try {
            ENTITY::class.java.getConstructor().newInstance()
        } catch (ex : NoSuchMethodException) {
            throw ORMesiException("Class ${ENTITY::class.simpleName} should have a no-arg constructor")
        }

        if(!Persistence.isAnnotatedWithPersistent(ENTITY::class)) {
            throw ORMesiException("Class ${ENTITY::class.simpleName} needs to be annotated with ${Persistent::class.qualifiedName}")
        }

        if(Persistence.getPrimaryKey(ENTITY::class.java.getConstructor().newInstance()).kotlinClass != PRIMARY::class) {
            throw ORMesiException("Repository for class ${ENTITY::class.simpleName} expects primary keys of type ${PRIMARY::class.qualifiedName}")
        }

        val repo =  BaseRepository<PRIMARY, ENTITY>(
                DatabaseConnectionFactory.create(system, connectionString),
                QueryBuilderFactory.create(system),
                ResultSetParser.create(system),
                RepositoryCache.create(),
                ENTITY::class
        )

        createdRepositories.add(repo)

        return repo
    }

    /**
     * wrapper for the [transactional] type safe builder
     */
    class TransactionalWrapper {
        internal fun executeBlock(block : TransactionalWrapper.() -> Unit) = block()
        fun commit() = {}
        inline fun rollback() { throw ORMesiException("transactional rollback") }
    }

    /**
     * persistent operations performed inside of [block] will be processed transactional
     */
    fun transactional(block : TransactionalWrapper.() -> Unit) {

        val wrapper = TransactionalWrapper()

        val transactions = createdRepositories.map { it.getTransaction() }
        transactions.forEach(RepositoryTransaction::begin)

        try {
            wrapper.executeBlock(block)
            transactions.forEach(RepositoryTransaction::commit)
        } catch (ex : Exception) {
            println(ex.message)
            transactions.forEach(RepositoryTransaction::rollback)
        }
    }
}

/**
 * persistent operations have to be executed inside of [block], connection to the database established by determing the [system] and [connectionString]
 */
fun persistenceContext(system: DatabaseSystem, connectionString: String, block : PersistenceContext.() -> Unit) {
    val context = PersistenceContext(system, connectionString)
    context.block()
}
