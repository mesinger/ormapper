package mesi.orm.persistence;

import lombok.*;
import mesi.orm.conn.TableEntry;

import java.util.ArrayList;
import java.util.List;
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
    private List<PersistentField> fields;

    @Getter(AccessLevel.PACKAGE)
    private Optional<PersistentStructure> parentStructure = Optional.empty();

    List<PersistentField> getAllFields() {

        var all = new ArrayList<PersistentField>();
        all.addAll(fields);

        parentStructure.ifPresent(parent -> all.addAll(parent.getAllFields()));

        return all;
    }
}
