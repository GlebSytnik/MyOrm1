package org.example;

import org.example.entity.TableItemEntity;
import org.example.enums.ItemTypeEnum;
import org.postgresql.Driver;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SqlPostgres {
    public static Driver driver = new Driver();
    private static final String url = "jdbc:postgresql://localhost:5432/my_orm";
    private static final String password = "djooky";
    private static final String userName = "djooky";

    public static void createTablemymethod(Object objectForTable) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             Statement statement = connection.createStatement()) {

            Class<? extends Object> carClass = objectForTable.getClass();
            Field[] declaredFields = carClass.getDeclaredFields();
            Map<String,Object> nameAndTypeObject = new HashMap<>();
            for (Field field : declaredFields) {
                nameAndTypeObject.put(field.getName(),field.getType());
            }
            String carClassName = carClass.getName();
            String[] words = carClassName.split("\\.");
            String className = words[words.length-1].toLowerCase(Locale.ROOT);

            DriverManager.registerDriver(driver);
        }
    }

    static String getCreateTableSQL(String tableName, List<TableItemEntity> tableItemEntities) {
        String sql = "create table if not exists " + tableName+"(\n";
        for(TableItemEntity tableItemEntity : tableItemEntities) {
            sql = sql + toSQL(tableItemEntity.getName(), tableItemEntity.getType(), tableItemEntity.getLength(), tableItemEntity.isPrimaryKey())+",\n";
        }
        return sql.substring(0, sql.length()-2)+"\n);";
    }

    private static String toSQL(String name, ItemTypeEnum type, long length, boolean isPrimaryKey){
        return name +" "+ type +" ("+length+") "+ (isPrimaryKey ?"PRIMARY KEY":"");
    }

    private static String getInsertSQL(String tableName, HashMap<String,String> value){
        String sql = "insert into "+ tableName +" (";
        String items = "";
        String itemValues = "";
        Set<String> set = value.keySet();
        for (String item : set) {
            String itemValue = value.get(item);
            if (!"".equals(itemValue)){
                items = items + item + ",";
                itemValues = itemValues + "'"+itemValue+"'" + ",";
            }
        }
        items = items.substring(0, items.length()-1)+")";
        itemValues = itemValues.substring(0, itemValues.length()-1)+");";
        return sql + items + " values(" + itemValues;
    }

    public static void main(String[] args) {



    }

}
