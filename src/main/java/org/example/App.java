package org.example;


import org.example.entity.Entity;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
       ManagerOrm.syncStructure("org.example.entity");
      //  Entity entity = new Entity("кот", "Мфтроскин", "matroskin.com");
        // Class<? extends Entity> entity1 = ManagerOrm.insert(entity);



    }
}

