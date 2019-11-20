package mesi.orm.persistence;

/**
 * supported table relation types
 */
public enum ForeignRelation {
    ONETOONE,
    ONETOMANY,
    MANYTOONE,
    MANYTOMANY
}
