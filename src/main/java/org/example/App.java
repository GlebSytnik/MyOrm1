package org.example;

import org.example.entity.TableItemEntity;
import org.example.enums.ItemTypeEnum;
import org.postgresql.Driver;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hello world!
 *
 */
public class App {
    private static final String url = "jdbc:postgresql://localhost:5432/my_orm";
    private static final String password = "djooky";
    private static final String userName = "djooky";
    public static Driver driver = new Driver();
    public static ResultSetMetaData metaData;

    public static void main( String[] args ) {

        try (Connection connection = DriverManager.getConnection(url, userName, password);
             Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                System.out.println("good");
            }

            DriverManager.registerDriver(driver);
         //  ResultSet resultSet = statement.executeQuery("select * from orm.data");
         //   metaData = resultSet.getMetaData();
           // String[] columnName = Util.getColumnNames(metaData);
            //statement.executeUpdate("create table entity (id varchar )");

            Entity entity = new Entity();
            Class<? extends Object> carClass = entity.getClass();

            Field[] declaredFields = carClass.getDeclaredFields();

          //  List<String> columnNameArray = new ArrayList<>();
           // List<Object> columnType = new ArrayList<>();
           // for (Field field : declaredFields) {
           //     columnNameArray.add(field.getName());
            //    columnType.add(field.getType());
          //  }
           // String carClassName = carClass.getName();
           // String[] words = carClassName.split("\\.");
           // String className = words[words.length-1];
            Map<String,Object> nameAndType = ParsingObject.getNameAndTypeField(entity);
            String createTable = "";
            for(Map.Entry<String, Object> item : nameAndType.entrySet()){
              createTable = " create table " + ParsingObject.getNameObject(entity)+ "(" + item.getKey()+ " " + item.getValue() + ")";


            }

         //   String createTable = " create table " + ParsingObject.getNameObject(entity)+ "(" + nameAndType.+ " " + columnType.get(0) + ")";
            TableItemEntity tableItemEntity = new TableItemEntity("Name", ItemTypeEnum.VARCHAR,125,false);
            TableItemEntity tableItemEntity2 = new TableItemEntity("Adress",ItemTypeEnum.VARCHAR,123,false);
            List<TableItemEntity> tableItemEntities = new ArrayList<>();
            tableItemEntities.add(tableItemEntity);
            tableItemEntities.add(tableItemEntity2);
            statement.executeUpdate(SqlPostgres.getCreateTableSQL("my_table",tableItemEntities));


        } catch (SQLException e) {
            System.out.println("Don't exit");
            //бросать ексепшен
            //внедрить транзакции
            //сделать в мапе
        }

        System.out.println( "Hello World!" );
    }
}
