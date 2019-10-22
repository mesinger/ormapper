package mesi.orm.conn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import mesi.orm.exception.ORMesiPersistenceException;
import mesi.orm.persistence.PersistenceManager;

import java.util.Arrays;
import java.util.Optional;

/***
 * data class which describes an
 * table used while creating
 * a sql statement for CREATE TABLE
 */
@Data
@AllArgsConstructor
public class TableEntry {
    @NonNull
    private String entryName;
    @NonNull
    private TableEntryType entryType;
    @NonNull
    private boolean nullable;
    @NonNull
    private boolean primary;
    @NonNull
    private boolean foreign;

    private String foreignTableName = "";
    private String foreignRef = "";

    /**
     * translates an objects type
     * to a supported database data type
     *
     * @param o
     * @return corresponding database entry type, or throws on invalid objects
     */
    public static TableEntryType getTypeOf(Object o) {

        var type = o.getClass();

        if(type.equals(Optional.class)) {
            type = ((Optional) o).get().getClass();
        }

        if (Number.class.isAssignableFrom(type)) {

            if (type.equals(double.class) || type.equals(Double.class) || type.equals(float.class) || type.equals(Float.class)) {
                return TableEntryType.DOUBLE;
            } else {
                return TableEntryType.INT;
            }
        } else if (type.equals(String.class)) {
            return TableEntryType.STRING;
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return TableEntryType.BOOL;
        }
        else {
            if(PersistenceManager.isObjectPersistent(type)) {
                return TableEntryType.INT;
            } else {
                throw new ORMesiPersistenceException("Cannot persist members of type " + type.getName());
            }
        }
    }
}

/***
 * base data types
 * that can be stored inside a
 * database system
 */
enum TableEntryType {
    INT,
    STRING,
    DOUBLE,
    BOOL
}

/***
 * translates table descriptors to form
 * parts of a sql query used for
 * CREATE TABLE
 */
interface TableEntryTranslator {

    static String sqlite(TableEntry... entries) {

        StringBuilder sql = new StringBuilder();

        sql.append("id INTEGER PRIMARY KEY AUTOINCREMENT, \n");

        Arrays.stream(entries)
                .map(entry -> (entry.isForeign() ? "fk_" : "") + entry.getEntryName() + " " +
                                TableEntryTypeTranslation.sqlite(entry.getEntryType()) +
                                TableEntryNullableTranslation.sqlite(entry.isNullable()) +
                                TableEntryPrimaryKeyTranslation.sqlite(entry.isPrimary()) + ", \n")
                .forEach(queryPart -> sql.append(queryPart));

        Arrays.stream(entries)
                .filter(entry -> entry.isForeign())
                .map(foreignKeyEntry -> TableEntryForeignKeyTranslation.sqlite(foreignKeyEntry.getEntryName(), foreignKeyEntry.getForeignTableName(), foreignKeyEntry.getForeignRef()) + ", \n")
                .forEach(queryPart -> sql.append(queryPart));

        //remove last three chars ", \n"
        sql.setLength(sql.length() - 3);

        return sql.toString();
    }
}

/***
 * static functions which translate
 * table entry types to a desired systems sql syntax
 */
interface TableEntryTypeTranslation {
    static String sqlite(TableEntryType type) throws IllegalArgumentException{

        if(type == null) {
            throw new IllegalArgumentException("type is null");
        }

        switch (type) {
            case INT:
            case BOOL:
                return "INTEGER";
            case STRING:
                return "TEXT";
            case DOUBLE:
                return "REAL";
            default:
                throw new IllegalArgumentException("Invalid Table Entry type");
        }
    }
}

/***
 * static functions which translate
 * nullability to a desired sql syntax
 */
interface TableEntryNullableTranslation {
    static String sqlite(boolean isNullable) {
        return isNullable ? "" : " NOT NULL";
    }
}

/***
 * static functions which translate
 * primary key entries to a desired sql syntax
 */
interface TableEntryPrimaryKeyTranslation {
    static String sqlite(boolean isPrimaryKey) {
        return isPrimaryKey ? " PRIMARY KEY AUTOINCREMENT" : "";
    }
}

/***
 * static functions which translate
 * foreign key entries to a desired sql syntax
 */
interface TableEntryForeignKeyTranslation {
    static String sqlite(String entryName, String foreignTableName, String foreignRef) {
        return "FOREIGN KEY (fk_" + entryName + ") REFERENCES " + foreignTableName + " (" + foreignRef + ")";
    }
}
