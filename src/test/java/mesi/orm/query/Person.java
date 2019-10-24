package mesi.orm.query;

import mesi.orm.persistence.Id;
import mesi.orm.persistence.Persistent;

@Persistent
class Person {
    @Id
    private Long id;
    private String name;
    private String surname;

    public Person(Long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}
