package mesi.orm.conn;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Arrays;

/***
 * data class which describes an
 * table entry used while creating
 * a sql statement for CREATE TABLE
 */
@AllArgsConstructor
public class TableEntry {
    @NonNull
    public final String entryName;
    @NonNull
    public final TableEntryType entryType;
    @NonNull
    public final boolean isNullable;
    @NonNull
    public final boolean isPrimaryKey;
    @NonNull
    public final boolean isForeignKey;
    public final String foreignTableName;
    public final String foreignRef;
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
 * translates entry(entries) to a
 * partly sql query, which can be executed
 * while CREATE TABLE
 */
interface TableEntryTranslation {

    static String sqlite(TableEntry... entries) {

        StringBuilder sql = new StringBuilder();

        Arrays.stream(entries)
                .map(entry -> entry.entryName + " " +
                                TableEntryTypeTranslation.sqlite(entry.entryType) +
                                TableEntryNullableTranslation.sqlite(entry.isNullable) +
                                TableEntryPrimaryKeyTranslation.sqlite(entry.isPrimaryKey) + ", \n")
                .forEach(queryPart -> sql.append(queryPart));

        Arrays.stream(entries)
                .filter(entry -> entry.isForeignKey)
                .map(foreignKeyEntry -> TableEntryForeignKeyTranslation.sqlite(foreignKeyEntry.entryName, foreignKeyEntry.foreignTableName, foreignKeyEntry.foreignRef) + ", \n")
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
        return isPrimaryKey ? " PRIMARY KEY" : "";
    }
}

/***
 * static functions which translate
 * foreign key entries to a desired sql syntax
 */
interface TableEntryForeignKeyTranslation {
    static String sqlite(String entryName, String foreignTableName, String foreignRef) {
        return "FOREIGN KEY (" + entryName + ") REFERENCES " + foreignTableName + " (" + foreignRef + ")";
    }
}
