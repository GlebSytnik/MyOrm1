package org.example;

import org.example.db.ConnectionHolderPostgres;

import java.sql.Connection;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        Entity entity1 = new Entity(1, "Roma", "Las", "gmail");
        Entity entity2 = new Entity(2, "Sam", "Rek", "gmail");
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            ManagerOrm.createTable(connection, entity1);
            ManagerOrm.insertTable(connection, entity1);
            ManagerOrm.insertTable(connection, entity2);
            ManagerOrm.updateTable(connection, 2, entity1);
            ManagerOrm.deleteById(connection, 1, entity1);
            Entity entity = ManagerOrm.getObjectById(connection, entity1, 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

