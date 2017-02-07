package com.example;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class GreenDaoGenerate {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.wujie.greendaogen");
        addPerson(schema);
        new DaoGenerator().generateAll(schema, "../GreenDao/app/src/main/java-gen");

    }

    private static void addPerson(Schema schema) {
        Entity person = schema.addEntity("Person");
        person.addIdProperty().primaryKey();
        person.addStringProperty("name").notNull();
        person.addStringProperty("sex").columnName("_sex");
    }
}
