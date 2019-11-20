package mesi.employee;

import mesi.orm.persistence.Id;
import mesi.orm.persistence.Nullable;
import mesi.orm.persistence.Persistent;

@Persistent
public class Person {
    @Id
    private Long id;
    private String name;
    private int age;
    private String uid;
    //    @Foreign
    @Nullable
    private Person other;

    public Person(String name, int age, String uid, Person other) {
        this.name = name;
        this.age = age;
        this.uid = uid;
        this.other = other;
    }

    public Person(String name, int age, String uid) {
        this.name = name;
        this.age = age;
        this.uid = uid;
    }

    public Person() {}
}