package mesi.orm.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PersistentStructureTest {

    private PersistentStructure structure;
    private PersistentStructure parent;

    private PersistentField field1;
    private PersistentField field2;
    private PersistentField parentfield1;
    private PersistentField parentfield2;

    @BeforeEach
    private void setup() {

        field1 = new PersistentField("field1", Optional.of("value"), false, false, false);
        field2 = new PersistentField("field2", Optional.of("value"), false, false, false);
        parentfield1 = new PersistentField("parentfield1", Optional.of("value"), false, false, false);
        parentfield2 = new PersistentField("parentfield2", Optional.of("value"), false, false, false);

        parent = new PersistentStructure("parenttablename", List.of(parentfield1, parentfield2));
        structure = new PersistentStructure("tablename", List.of(field1, field2), Optional.of(parent));
    }

    @Test
    public void testParentStructureContained() {
        assertEquals(parent, structure.getParentStructure().get());
    }

    @Test
    public void testFieldSizes() {
        assertEquals(2, structure.getFields().size());
        assertEquals(2, parent.getFields().size());
        assertEquals(4, structure.getAllFields().size());
        assertEquals(2, parent.getAllFields().size());
    }

    @Test
    public void testGetAllFields() {
        var allFields = structure.getAllFields();
        assertTrue(allFields.containsAll(List.of(field1, field2, parentfield1, parentfield2)));
    }

    @Test
    public void testGetPersistentStrucutresId() {

        final Long ID = 21323L;
        final var idField = new PersistentField("id", Optional.of(ID), false, true, false);
        final var psWithId = new PersistentStructure("tablename", List.of(idField, field1));

        assertEquals(ID, psWithId.getPersistentStrucutreId().orElse(0L));
        assertEquals(Optional.empty(), structure.getPersistentStrucutreId());
    }

    @Test
    public void testHasId() {

        final Long ID = 21323L;
        final var idField = new PersistentField("id", Optional.of(ID), false, true, false);
        final var psWithId = new PersistentStructure("tablename", List.of(idField, field1));

        assertTrue(psWithId.hasId());
        assertFalse(structure.hasId());
    }
}
