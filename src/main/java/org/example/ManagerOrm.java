package org.example;

import org.example.db.ConnectionHolderPostgres;

import java.sql.SQLException;

public class ManagerOrm {

    public static void createTable(Object object) {
        try {
            SqlPostgres.executeSQL(ConnectionHolderPostgres.getConnection(), SqlPostgres.createTable(object));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void insertTable(Object object) {
        SqlPostgres.executeSQL(ConnectionHolderPostgres.getConnection(), SqlPostgres.getInsertSql(object, ParsingObject.getNameAndValueField(object)));
    }

    public static void updateTable(int id, Object object) {
        SqlPostgres.executeSQL(ConnectionHolderPostgres.getConnection(), SqlPostgres.getSqlUpdate(id, object, ParsingObject.getNameAndValueField(object)));
    }

    public static void deleteById(int id, Object object) {
        SqlPostgres.executeSQL(ConnectionHolderPostgres.getConnection(), SqlPostgres.getStringDeleteById(id, object));
    }
}
