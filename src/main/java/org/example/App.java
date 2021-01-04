package org.example;

public class App {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Entity entity1 = new Entity(1, "Roma", "Las", "gmail");
        Entity entity2 = new Entity(2, "" +
                "Sam", "Rek", "gmail");
        ManagerOrm.createTable(entity1);
        ManagerOrm.insertTable(entity1);
        ManagerOrm.insertTable(entity2);
        ManagerOrm.updateTable(2, entity1);
        ManagerOrm.deleteById(1, entity1);
        Entity entity = ManagerOrm.getObjectById(entity1, 2);
    }
}

