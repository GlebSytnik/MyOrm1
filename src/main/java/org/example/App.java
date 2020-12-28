package org.example;

public class App {

    public static void main(String[] args) {

        Entity entity1 = new Entity(1, "Roma", "Las", "gmail");
        Entity entity2 = new Entity(2,"" +
                "Sam","Rek","gmail");
        ManagerOrm.createTable(entity1);
        ManagerOrm.insertTable(entity1);
        ManagerOrm.insertTable(entity2);
    }
}

