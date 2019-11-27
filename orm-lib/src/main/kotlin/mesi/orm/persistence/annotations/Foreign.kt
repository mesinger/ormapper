package mesi.orm.persistence.annotations

/**
 * identifies a foreign persistent property
 * and their relation to another class
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Foreign(val relation : ForeignRelation)

enum class ForeignRelation {
    ONE_TO_ONE,
    ONE_TO_MANY,
    MANY_TO_ONE,
    MANY_TO_MANY
}
