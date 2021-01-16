package org.example;

import org.example.entity.Entity;

public class App {

    public static void main(String[] args)  {
        ManagerOrm.syncStructure("org.example.entity");
        Entity entity = new Entity("Кот", "Матроскин", "matroskin.com");
        entity = ManagerOrm.insert(entity);
        Entity entity1 = ManagerOrm.getObjectById(entity, 1);
    }
}

