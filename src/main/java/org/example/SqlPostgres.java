package org.example;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

public class SqlPostgres {

    public static boolean executeSQL(Connection connection, String sql) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
        return true;
    }

    public static String createTable(Object objectForTable) throws SQLException {
        return getSqlQueryCreate(ParsingObject.getNameAndTypeField(objectForTable), objectForTable);
    }

    public static String getSqlQueryCreate(Map<String, Object> nameAndTypeField, Object objectForTable) {
        String tableName = ParsingObject.getNameTable(objectForTable);
        String fields = createFields(nameAndTypeField);
        return String.format("create table if not exists %s ( %s );", tableName, fields);
    }

    private static String createFields(Map<String, Object> nameAndTypeField) {
        StringBuilder fields = new StringBuilder();
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

    static String getInsertSql(Object objectForTable, Map<String, String> nameAndValueField) {
        String nameTable = ParsingObject.getNameTable(objectForTable);
        String valueField = getInsertFieldsValueSqlBuilder(nameAndValueField);
        String nameField = getInsertFieldsNameSqlBuilder(nameAndValueField);
        String result = String.format("insert into  %s (%s) VALUES ( %s );", nameTable, nameField, valueField);
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
        String nameTable = ParsingObject.getNameTable(objectForTable);
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
        String name = ParsingObject.getNameTable(objectForTable);
        String result = String.format("DELETE FROM %s WHERE id = %d", name, id);
        return result;
    }

    static String getStringFindById(int id, Object objectForTable) {
        String name = ParsingObject.getNameTable(objectForTable);
        String result = String.format("SELECT * FROM %s WHERE id = %d", name, id);
        return result;
    }

    static Set<String> getSetNameBase(Connection connection, Object object, int id) {
        try {
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
            throwables.printStackTrace();
        }
        return null;
    }

    static List<Object> getListValueBase(Connection connection, Object object, int id) {
        try {
            String sql = getStringFindById(id, object);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<Object> listValue = new ArrayList<>();
            Set<String> nameColumnBase = getSetNameBase(connection, object, id);
            if (rs.next()) {
                for (String nameObject : nameColumnBase) {
                    listValue.add(rs.getObject(nameObject));
                }
            }
            return listValue;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static <T> T createConcreteObject(T object) {
        T concreteObject = (T) object;
        return concreteObject;
    }

    static <T> T getObjectById(Connection connection, T object, int id) throws IllegalAccessException {
        List<Object> listValue = getListValueBase(connection, object, id);
        T concreteObject = (T) object;
        Field[] fields = concreteObject.getClass().getDeclaredFields();
        int i = 0;
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(concreteObject) == null) {
                f.set(concreteObject, listValue.get(i++));
            }
        }
        return concreteObject;
    }
}



