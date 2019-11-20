package mesi.employee;

import mesi.orm.conn.RDBMS;
import mesi.orm.persistence.*;

public class App {

    public static void main(String[] args) {

        PersistenceManager pm = PersistenceManagerFactory.create(RDBMS.SQLITE, "jdbc:sqlite:employees.db");

//        Person salami = new Person("salami" ,1, "pepperoni", null);
//        Person mesi = new Person("mesi", 23, "if17b021", salami);

        Person mesi = new Person("bauer", 23, "if17b022");
//        pm.persist(mesi);

        var persons = pm.from(Person.class).where("uid='if17b022'").fetch();
    }
}
