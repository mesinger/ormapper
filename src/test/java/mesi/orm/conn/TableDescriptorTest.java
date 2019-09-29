package mesi.orm.conn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableDescriptorTest {

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
        assertEquals(" PRIMARY KEY", TableDescriptorPrimaryKeyTranslation.sqlite(true));
        assertEquals("", TableDescriptorPrimaryKeyTranslation.sqlite(false));
    }

    @Test
    public void testEntryForeignKeyTranslation() {
        final String entryName = "entryname";
        final String foreignTableName = "foreigntable";
        final String foreignRef = "id";
        final String expected = "FOREIGN KEY (" + entryName + ") REFERENCES " + foreignTableName + " (" + foreignRef + ")";

        assertEquals(expected, TableDescriptorForeignKeyTranslation.sqlite(entryName, foreignTableName, foreignRef));
    }
}
