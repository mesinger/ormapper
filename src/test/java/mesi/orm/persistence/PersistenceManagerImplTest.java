package mesi.orm.persistence;

import mesi.orm.conn.RDBMS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceManagerImplTest {

    private PersistenceManagerImpl pm;

    @BeforeEach
    private void setup() {
        pm = new PersistenceManagerImpl();
    }

    @Test
    public void testGetPersistentStructureOf() throws Exception {

        Method pGetPersistentStructureOf = pm.getClass().getDeclaredMethod("getPersistentStructureOf", Object.class, Class.class);
        pGetPersistentStructureOf.setAccessible(true);

        final PersistentStructure actual = (PersistentStructure) pGetPersistentStructureOf.invoke(pm, new Student("mesi", 23, "if17b021"), Student.class);

        final var map1 = new HashMap<String, Object>();
        map1.put("id", null);
        map1.put("uid", "if17b021");

        final var map2 = new HashMap<String, Object>();
        map2.put("id", null);
        map2.put("name", "mesi");
        map2.put("age", 23);

        final var expected = new PersistentStructure(
                Student.class.getName() + "_table",
                map1,
                Optional.of(new PersistentStructure(
                        Person.class.getName() + "_table",
                        map2
                ))
        );

        assertEquals(expected, actual);
    }

    @Test
    public void testGetPersistenceObjectsTableName() throws Exception {

        Method pGetPersistenceObjectsTableName = pm.getClass().getDeclaredMethod("getPersistenceObjectsTableName", Object.class);
        pGetPersistenceObjectsTableName.setAccessible(true);

        final String className = Object.class.getName();
        assertEquals(className + "_table", pGetPersistenceObjectsTableName.invoke(pm, new Object()));
    }
}

@Persistent
class Person {
    @Id
    private Long id;
    protected String name;
    protected int age;
}

@Persistent
class Student extends Person {
    @Id
    private Long id;
    protected String uid;

    Student(String name, int age, String uid) {
        this.name = name;
        this.age = age;
        this.uid = uid;
    }
}
