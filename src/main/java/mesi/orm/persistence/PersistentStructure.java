package mesi.orm.persistence;

import lombok.*;
import mesi.orm.conn.TableEntry;

import java.util.Map;
import java.util.Optional;

/**
 * objects of this class
 * describe fields and inheritence
 * of persistent objects
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
class PersistentStructure {

    @NonNull
    private String tableName;

    @NonNull
    private Map<String, PersistentField> fields;

    @Getter(AccessLevel.PACKAGE)
    private Optional<PersistentStructure> parentStructure = Optional.empty();

//    TableEntry[] translate
}

@Data
class PersistentField {
    @NonNull
    private Object value;
    @NonNull
    private boolean isNullable;
    @NonNull
    private boolean isPrimary;
    @NonNull
    private boolean isForeign;
}
