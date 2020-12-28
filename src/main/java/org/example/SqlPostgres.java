package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class SqlPostgres {

    public static boolean executeSQL(Connection connection, String sql){
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
        return String.format("create table if not exists %s ( %s );" , tableName, fields);
    }

    private static String createFields(Map<String, Object> nameAndTypeField) {
        StringBuilder fields = new StringBuilder();
        Iterator<Map.Entry<String, Object>> itr = nameAndTypeField.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> next = itr.next();
            fields.append(next.getKey()).append(" ").append(ParsingObject.getTypeObject(next.getValue()));
            if (itr.hasNext()){
                fields.append(",");
            }
        }
        return fields.toString();
    }

    static String getInsertSQL(Object objectForTable, Map<String, String> nameAndValueField) {
        String sql = "insert into " + ParsingObject.getNameTable(objectForTable) + " (";
        String items = "";
        String itemValues = "";
        Set<String> setNameAndValue = nameAndValueField.keySet();
        for (String item : setNameAndValue) {
            String itemValue = nameAndValueField.get(item);
            if (!"".equals(itemValue)) {
                items = items + item + ",";
                itemValues = itemValues + "'" + itemValue + "'" + ",";
            }
        }
        items = items.substring(0, items.length() - 1) + ")";
        itemValues = itemValues.substring(0, itemValues.length() - 1) + ");";
        String result = sql + items + " VALUES(" + itemValues;
        return result;
    }

    static  String getInsertFieldsNameSqlBuilder( Map<String, String> nameAndValueField){
        StringBuilder fieldsName = new StringBuilder();
        StringBuilder fieldsValue = new StringBuilder();
        Iterator<Map.Entry<String, String>> itr = nameAndValueField.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<String, String> next = itr.next();
            fieldsName.append(next.getKey()).append(" ");
            fieldsValue.append(next.getValue());
            if (itr.hasNext()){
                fieldsName.append(",");
                fieldsValue.append( ",");
            }

        }
        //fieldsValue.append(")");
        String result =fieldsName.toString();
        return result;
    }
    static  String getInsertFieldsValueSqlBuilder( Map<String, String> nameAndValueField){
        StringBuilder fieldsName = new StringBuilder();
        StringBuilder fieldsValue = new StringBuilder();
        Iterator<Map.Entry<String, String>> itr = nameAndValueField.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<String, String> next = itr.next();
            fieldsName.append(next.getKey()).append(" ");
            fieldsValue.append("'" + next.getValue()+"'").append(" ");
            if (itr.hasNext()){
                fieldsName.append(",");
                fieldsValue.append( ",");
            }

        }
        //fieldsValue.append(")");
        String result =fieldsValue.toString();
        return result;
    }

    static String getInsertSqlMy(Object objectForTable, Map<String, String> nameAndValueField){
        String nameTable = ParsingObject.getNameTable(objectForTable);
        String valueField = getInsertFieldsValueSqlBuilder(nameAndValueField);
        String nameField = getInsertFieldsNameSqlBuilder(nameAndValueField);
        String result = String.format("insert into  %s (%s) VALUES   ( %s );" , nameTable,nameField, valueField);
        return result;

    }

    public static void main(String[] args) {
        Entity entity2 = new Entity(3,"qwe","asd","gmail");
        Map<String,String> map = new LinkedHashMap<>();
        map.put("id","1");
        map.put("name" , "Sam");
        getInsertSqlMy(entity2,map);
    }

}


