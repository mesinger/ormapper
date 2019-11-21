package mesi.orm.persistence.annotations

/**
 * identifies the primary key
 * in an persistent class
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Primary
