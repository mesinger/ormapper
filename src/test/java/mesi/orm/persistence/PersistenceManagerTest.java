package mesi.orm.persistence;

import mesi.orm.conn.DatabaseConnectionFactory;
import mesi.orm.conn.RDBMS;
import mesi.orm.exception.ORMesiPersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PersistenceManagerTest {

    @Mock
    private DatabaseConnectionFactory databaseConnectionFactory;

    @InjectMocks
    private ValidPersistenceManager validpm;

    @InjectMocks
    private MissingDatabaseAnnotationPersistenceManager missingdbannopm;

    @Test
    public void testValidateDatabaseAnnotation() throws Exception {
        Method pValidateDatabaseAnnotation = validpm.getClass().getDeclaredMethod("validateDatabaseAnnotation");

        // this should not throw
        pValidateDatabaseAnnotation.invoke(validpm);
    }

    @Test
    public void testMissingDatabaseAnnotation() throws Exception {
        Method pValidateDatabaseAnnotation = missingdbannopm.getClass().getDeclaredMethod("validateDatabaseAnnotation");

        assertThrows(
                ORMesiPersistenceException.class,
                () -> {
                    try {
                        pValidateDatabaseAnnotation.invoke(missingdbannopm);
                    } catch (InvocationTargetException ex) {
                        throw ex.getCause();
                    }
                }
        );
    }

    @Test
    public void test

    @Test
    public void testPersist() {

        assertThrows(ORMesiPersistenceException.class, () ->validpm.persist(new NotPersistentObject()));
        assertThrows(ORMesiPersistenceException.class, () ->validpm.persist(new PersistentObject()));

        throw new RuntimeException("todo");
    }

    @Test
    public void testMocks() {
        assertNotNull(databaseConnectionFactory);
        assertNotNull(validpm);
        assertNotNull(missingdbannopm);
    }
}

@Database(
        system = RDBMS.SQLITE
)
class ValidPersistenceManager extends PersistenceManager {
    public ValidPersistenceManager(DatabaseConnectionFactory factroy) {
        super(factroy);
    }

    @Override
    protected void validateDatabaseAnnotation() {
        super.validateDatabaseAnnotation();
    }
}

class MissingDatabaseAnnotationPersistenceManager extends PersistenceManager {
    public MissingDatabaseAnnotationPersistenceManager(DatabaseConnectionFactory factroy) {
        super(factroy);
    }

    @Override
    void validateDatabaseAnnotation() {
        super.validateDatabaseAnnotation();
    }
}
