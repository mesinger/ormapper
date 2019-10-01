package mesi.orm.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PersistentField {
    @NonNull
    private String name;
    @NonNull
    private Object value;
    @NonNull
    private boolean isNullable;
    @NonNull
    private boolean isPrimary;
    @NonNull
    private boolean isForeign;

    private String foreignTableName = "";
    private String foreignRef = "";
}
