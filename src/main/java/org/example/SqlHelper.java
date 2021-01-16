package org.example;

import org.apache.log4j.Logger;
import org.example.db.ConnectionHolderPostgres;
import org.example.exception.BadConnectionExeception;
import org.example.exception.NotFieldException;
import org.example.exception.NotListValueBaseException;
import org.example.exception.NotSetNameBaseException;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class SqlHelper {

    private static Logger logger = Logger.getLogger(SqlHelper.class);

    public static boolean executeSQL(String sql) {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.execute();
        } catch (SQLException throwable) {
            logger.info("Check method executeSQL");
            throw new BadConnectionExeception("Bad connection");
        }
    }

    public static long executeInsert(String sql) {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            long id = 0;
            while (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            return id;
        } catch (SQLException e) {
            logger.info("Check method executeInsert");
            throw new BadConnectionExeception("Bad connection");
        }
    }

    public static String createTable(Class objectForTable) throws SQLException {
        return getSqlQueryCreate(ParsingObject.getNameAndTypeField(objectForTable), objectForTable);
    }

    public static String dropTable(Class objectForTable) {
        return getSqlQueryDrop(objectForTable);
    }

    public static String getSqlQueryDrop(Class objectForTable) {
        String tableName = ParsingObject.getNameTable(objectForTable);
        String result = String.format("DROP table if exists %s;", tableName);
        return result;
    }

    public static String getSqlQueryCreate(Map<String, Object> nameAndTypeField, Class objectForTable) {
        String tableName = ParsingObject.getNameTable(objectForTable);
        String fields = createFields(nameAndTypeField);
        String result = String.format("create table if not exists  %s ( %s ) ;", tableName, fields);
        return result;
    }

    private static String createFields(Map<String, Object> nameAndTypeField) {
        StringBuilder fields = new StringBuilder();
        fields.append("id SERIAL ,");
        Iterator<Map.Entry<String, Object>> itr = nameAndTypeField.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> next = itr.next();
            fields.append(next.getKey()).append(" ").append(ParsingObject.getTypeObject(next.getValue()));
            if (itr.hasNext()) {
                fields.append(",");
            }
        }
        return fields.toString();
    }

    static String getInsertSqlStringReturnId(Object objectForTable, Map<String, String> nameAndValueField) {
        String nameTable = ParsingObject.getNameTable(objectForTable);
        String valueField = getInsertFieldsValueSqlBuilder(nameAndValueField);
        String nameField = getInsertFieldsNameSqlBuilder(nameAndValueField);
        String result = String.format("insert into  %s (%s) VALUES ( %s ) RETURNING id;", nameTable, nameField, valueField);
        return result;
    }

    static String getInsertFieldsNameSqlBuilder(Map<String, String> nameAndValueField) {
        StringBuilder fieldsName = new StringBuilder();
        Iterator<Map.Entry<String, String>> itr = nameAndValueField.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> next = itr.next();
            fieldsName.append(next.getKey()).append(" ");
            if (itr.hasNext()) {
                fieldsName.append(",");
            }
        }
        String result = fieldsName.toString();
        return result;
    }

    static String getInsertFieldsValueSqlBuilder(Map<String, String> nameAndValueField) {
        StringBuilder fieldsValue = new StringBuilder();
        Iterator<Map.Entry<String, String>> itr = nameAndValueField.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> next = itr.next();
            fieldsValue.append("'" + next.getValue() + "'").append(" ");
            if (itr.hasNext()) {
                fieldsValue.append(",");
            }
        }
        String result = fieldsValue.toString();
        return result;
    }

    static String getSqlUpdate(int id, Object objectForTable, Map<String, String> nameAndValueField) {
        String nameTable = ParsingObject.getNameTable(objectForTable.getClass());
        String update = getFieldAndValueSqlUpdate(nameAndValueField);
        String result = String.format("update %s  set  %s  WHERE id = %d ;", nameTable, update, id);
        return result;
    }

    static String getFieldAndValueSqlUpdate(Map<String, String> nameAndValueField) {
        StringBuilder updateData = new StringBuilder();
        Iterator<Map.Entry<String, String>> itr = nameAndValueField.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, String> next = itr.next();
            updateData.append(next.getKey()).append(" = ").append("'" + next.getValue() + "'").append(" ");
            if (itr.hasNext()) {
                updateData.append(",");
            }
        }
        String result = updateData.toString();
        return result;
    }

    static String getStringDeleteById(int id, Object objectForTable) {
        String name = ParsingObject.getNameTable(objectForTable.getClass());
        String result = String.format("DELETE FROM %s WHERE id = %d", name, id);
        return result;
    }

    static String getStringFindById(long id, Object objectForTable) {
        String name = ParsingObject.getNameTable(objectForTable);
        String result = String.format("SELECT * FROM %s WHERE id = %d", name, id);
        return result;
    }

    static Set<String> getSetNameBase(Object object, long id) {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            String sql = getStringFindById(id, object);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Set<String> nameSetBase = new LinkedHashSet<>();

            for (int i = 1; i <= columnCount; i++) {
                String nameColumn = rsmd.getColumnName(i);
                nameSetBase.add(nameColumn);
            }
            return nameSetBase;
        } catch (SQLException throwables) {
            logger.info("Check methods getSetNameBase");
            throw new NotSetNameBaseException("These names are not in the database");
        }
    }

    static List<Object> getListValueBase(Object objectForName, long id) {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            String sql = getStringFindById(id, objectForName);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<Object> listValue = new ArrayList<>();
            Set<String> nameColumnBase = getSetNameBase(objectForName, id);
            if (rs.next()) {
                for (String nameObject : nameColumnBase) {
                    listValue.add(rs.getObject(nameObject));
                }
            }
            return listValue;
        } catch (SQLException e) {
            throw new NotListValueBaseException("This object not value in database");
        }
    }

    static <T> T createConcreteObject(Class object) {
        Class T = null;
        T concreteObject = null;
        try {
            concreteObject = (T) object.newInstance();
        } catch (InstantiationException e) {
            logger.info("This object don't instance,check method createConcreteObject", e);
        } catch (IllegalAccessException e) {
            throw new NotFieldException("This field don't exist");
        }
        return concreteObject;
    }

    static <T> T getObjectById(Object objectForName, long id) throws IllegalAccessException {
        List<Object> listValue = getListValueBase(objectForName, id);
        T concreteObject = null;
        try {
            concreteObject = (T) objectForName.getClass().newInstance();
        } catch (InstantiationException e) {
            logger.info("This object don't instance,check method getObjectById", e);
        }
        Field[] fields = concreteObject.getClass().getSuperclass().getDeclaredFields();

        int i = 0;
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(concreteObject) == null) {
                f.set(concreteObject, new Long((Integer) listValue.get(i++)));
            }
        }
        Field[] fieldsEntity = concreteObject.getClass().getDeclaredFields();

        int g = 1;
        for (Field f : fieldsEntity) {
            f.setAccessible(true);
            if (f.get(concreteObject) == null) {
                f.set(concreteObject, listValue.get(g++));
            }
        }
        return concreteObject;
    }

    public static Set<String> getSetTableWithDataBase() {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            Set<String> nameTable = new LinkedHashSet<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT table_name\n" +
                    "  FROM information_schema.tables\n" +
                    " WHERE table_schema='public'\n" +
                    "   AND table_type='BASE TABLE';");
            while (resultSet.next()) {
                nameTable.add(resultSet.getString(1));
            }
            return nameTable;
        } catch (SQLException throwables) {
            logger.info("Check methods getSetTableWithDataBase");
            throw new BadConnectionExeception("Bad connection in methods createTable");
        }
    }
}



