package mesi.orm.conn;

import mesi.orm.exception.ORMesiPersistenceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableEntryTest {

    @Test
    public void testEntryTypeTranslationSqlite() {
        assertEquals("INTEGER", TableEntryTypeTranslation.sqlite(TableEntryType.INT));
        assertEquals("TEXT", TableEntryTypeTranslation.sqlite(TableEntryType.STRING));
        assertEquals("REAL", TableEntryTypeTranslation.sqlite(TableEntryType.DOUBLE));
        assertEquals("INTEGER", TableEntryTypeTranslation.sqlite(TableEntryType.BOOL));
        assertThrows(IllegalArgumentException.class, () -> TableEntryTypeTranslation.sqlite(null));
    }

    @Test
    public void testEntryPrimaryKeyTranslation() {
        assertEquals(" PRIMARY KEY AUTOINCREMENT", TableEntryPrimaryKeyTranslation.sqlite(true));
        assertEquals("", TableEntryPrimaryKeyTranslation.sqlite(false));
    }

    @Test
    public void testEntryForeignKeyTranslation() {
        final String entryName = "entryname";
        final String foreignTableName = "foreigntable";
        final String foreignRef = "id";
        final String expected = "FOREIGN KEY (fk_" + entryName + ") REFERENCES " + foreignTableName + " (" + foreignRef + ")";

        assertEquals(expected, TableEntryForeignKeyTranslation.sqlite(entryName, foreignTableName, foreignRef));
    }

    @Test
    public void testGetTypeOf() {
        assertEquals(TableEntryType.DOUBLE, TableEntry.getTypeOf(Double.valueOf(1.0)));
        assertEquals(TableEntryType.DOUBLE, TableEntry.getTypeOf(1.0));
        assertEquals(TableEntryType.DOUBLE, TableEntry.getTypeOf(Float.valueOf(1.0f)));
        assertEquals(TableEntryType.DOUBLE, TableEntry.getTypeOf(1.0f));

        assertEquals(TableEntryType.INT, TableEntry.getTypeOf(Integer.valueOf(1)));
        assertEquals(TableEntryType.INT, TableEntry.getTypeOf(1));
        assertEquals(TableEntryType.INT, TableEntry.getTypeOf(Long.valueOf(1)));
        assertEquals(TableEntryType.INT, TableEntry.getTypeOf(1L));
        assertEquals(TableEntryType.INT, TableEntry.getTypeOf(Short.valueOf((short)1)));
        assertEquals(TableEntryType.INT, TableEntry.getTypeOf((short)1));

        assertEquals(TableEntryType.STRING, TableEntry.getTypeOf(String.valueOf("string")));
        assertEquals(TableEntryType.STRING, TableEntry.getTypeOf(new String("string")));
        assertEquals(TableEntryType.STRING, TableEntry.getTypeOf("string"));

        assertEquals(TableEntryType.BOOL, TableEntry.getTypeOf(Boolean.valueOf(true)));
        assertEquals(TableEntryType.BOOL, TableEntry.getTypeOf(true));

        assertThrows(ORMesiPersistenceException.class, () -> TableEntry.getTypeOf(new char[]{'s', 't', 'r', 'i', 'n', 'g'}));
        assertThrows(ORMesiPersistenceException.class, () -> TableEntry.getTypeOf(new StringBuilder("string")));
    }
}
