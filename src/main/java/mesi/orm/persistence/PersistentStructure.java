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

    /**
     * @return all fields and parent fields recursively
     */
    List<PersistentField> getAllFields() {

        var all = new ArrayList<PersistentField>();
        all.addAll(fields);

        parentStructure.ifPresent(parent -> all.addAll(parent.getAllFields()));

        return all;
    }

    Optional<Long> getPersistentStrucutreId() {
        try {
            return getAllFields().stream()
                    .filter(field -> field.isPrimary())
                    .map(field -> (Long) field.getValue())
                    .findFirst();
        } catch (ClassCastException ex) {
            return Optional.empty();
        }
    }

    void setPrimaryKey(Long id) {
        fields.removeIf(field -> field.getName().toLowerCase().equals("id") && field.isPrimary());
        fields.add(
                new PersistentField(
                        "id",
                        Optional.of(id),
                        false,
                        true,
                        false
                )
        );
    }
}
