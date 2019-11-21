package mesi.orm.persistence.annotations

/**
 * decorative annotation for persistent class properties, which should be ignored by the mapper
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PersistenceTransient