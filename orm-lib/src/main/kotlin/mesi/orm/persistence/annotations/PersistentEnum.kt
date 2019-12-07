package mesi.orm.persistence.annotations

/**
 * decorative annotation for an enum in an persistent class
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PersistentEnum