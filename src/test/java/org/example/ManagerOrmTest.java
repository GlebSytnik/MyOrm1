package org.example;

import org.example.db.ConnectionHolderPostgres;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ManagerOrmTest {

    @Test
    public void getObjectById() {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            Entity entity = new Entity(1, "Roma", "Las", "gmail");
            Entity entityExpected = ManagerOrm.getObjectById(connection, entity, 2);
            Entity entityActual = entity;
            assertTrue(entityExpected.equals(entityActual));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}