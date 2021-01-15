package org.example;

import org.example.entity.Entity;
import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
    // ManagerOrm.syncStructure("org.example.entity");

        Entity entity = new Entity("Кот", "Матроскин", "matroskin.com");
        Entity entity1 = ManagerOrm.insert(entity);
        entity1 = ManagerOrm.getObjectById(Entity.class,1);
    }
}

