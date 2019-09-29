package mesi.orm.persistence;

import lombok.*;
import mesi.orm.conn.TableDescriptor;

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
    private Map<String, Object> fields;

    @Getter(AccessLevel.PACKAGE)
    private Optional<PersistentStructure> parentStructure = Optional.empty();
}
