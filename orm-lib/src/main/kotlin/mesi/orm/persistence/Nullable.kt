package mesi.orm.persistence

/**
 * decorative annotation for persistent class properties, which can be null
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Nullable