package org.example;

import org.example.exception.BadConnectionExeception;

import java.sql.Connection;
import java.sql.SQLException;

public class ManagerOrm {

    public static void createTable(Connection connection, Object object) {
        try {
            SqlPostgres.executeSQL(connection, SqlPostgres.createTable(object));
        } catch (SQLException throwables) {
            new BadConnectionExeception("Bad connection in methods createTable");
        }
    }

    public static void insertTable(Connection connection, Object object) {
        SqlPostgres.executeSQL(connection, SqlPostgres.getInsertSql(object, ParsingObject.getNameAndValueField(object)));
    }

    public static void updateTable(Connection connection, int id, Object object) {
        SqlPostgres.executeSQL(connection, SqlPostgres.getSqlUpdate(id, object, ParsingObject.getNameAndValueField(object)));
    }

    public static void deleteById(Connection connection, int id, Object object) {
        SqlPostgres.executeSQL(connection, SqlPostgres.getStringDeleteById(id, object));
    }

    public static <T> T getObjectById(Connection connection, T object, int id) {
        try {
            return SqlPostgres.getObjectById(connection, object, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
