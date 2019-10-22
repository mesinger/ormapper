package mesi.orm.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * data class for storing information
 * about a member in a persistent object
 * (mainly used while parsing annotations, name, object type, value) of persistent members)
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PersistentField {
    @NonNull
    private String name;
    @NonNull
    private Optional<Object> value;
    @NonNull
    private boolean isNullable;
    @NonNull
    private boolean isPrimary;
    @NonNull
    private boolean isForeign;

    private String foreignTableName = "";
    private String foreignRef = "";
}
