package mesi.orm.query;

import mesi.orm.persistence.transform.PersistentObject;
import mesi.orm.persistence.transform.PersistentProperty;
import mesi.orm.persistence.transform.PersistentPropertyType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class SQLiteInsertQuery extends InsertQuery {

    protected SQLiteInsertQuery(PersistentObject persistentObject) {
        super(persistentObject);

        head.append("INSERT INTO " + persistentObject.getTableName() + " (");

        persistentObject.getProperties().stream()
                .map(property -> property.getName())
                .forEach(name -> head.append(name + ", "));

        head.setLength(head.length() - 2);
        head.append(") ");

        tail.append("VALUES (");

        persistentObject.getProperties().stream()
                .map(this::mapValue)
                .map(property -> property.getValue())
                .forEach(value -> tail.append(value + ", "));

        tail.setLength(tail.length() - 2);
        tail.append(");");
    }

    private PersistentProperty mapValue(PersistentProperty property) {

        if(property.getType().equals(PersistentPropertyType.STRING)) {
            property.setValue("'" + property.getValue() + "'");
        }
        else if(property.getType().equals(PersistentPropertyType.TIME)) {
            var modifiedProperty = (LocalTime) property.getValue();
            property.setValue("'" + modifiedProperty.format(DateTimeFormatter.ISO_LOCAL_TIME) + "'");
        }
        else if(property.getType().equals(PersistentPropertyType.DATE)) {
            var modifiedProperty = (LocalDate) property.getValue();
            property.setValue("'" + modifiedProperty.format(DateTimeFormatter.ISO_LOCAL_DATE) + "'");
        }
        else if(property.getType().equals(PersistentPropertyType.DATETIME)) {
            var modifiedProperty = (LocalDateTime) property.getValue();
            property.setValue("'" + modifiedProperty.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "'");
        }

        return property;
    }
}
