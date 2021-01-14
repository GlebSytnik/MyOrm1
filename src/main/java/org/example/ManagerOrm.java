package org.example;

import org.example.db.ConnectionHolderPostgres;
import org.example.exception.BadConnectionExeception;
import org.example.exception.NotValueObjectException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class ManagerOrm {

    public static void createTable(Class object) {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            SqlHelper.executeSQL(connection, SqlHelper.createTable(object));
        } catch (SQLException throwables) {
            new BadConnectionExeception("Bad connection in methods createTable");
        }
    }

    public static void dropTable(Class object){
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            SqlHelper.executeSQL(connection, SqlHelper.dropTable(object));
        } catch (SQLException throwables) {
            new BadConnectionExeception("Bad connection in methods createTable");
        }
    }

    public static <T> T insert(Object objectClass) {

        Long id = SqlHelper.executeInsert(SqlHelper.getInsertSqlStringReturnId(objectClass, ParsingObject.getNameAndValueField(objectClass)));
        Class<?> classObjectParents = objectClass.getClass().getSuperclass();
        Field field;
        try {
            field = classObjectParents.getDeclaredField("id");
            field.setAccessible(true);
            field.set(objectClass, id);
            return (T) objectClass;
        } catch (Exception e) {
            throw new NotValueObjectException("This object does not value");
        }
    }

    public static void update(Connection connection, Object object, int id) {

        SqlHelper.executeSQL(connection, SqlHelper.getSqlUpdate(id, object, ParsingObject.getNameAndValueField(object)));
    }

    public static void deleteById(Connection connection, int id, Object object) {
        SqlHelper.executeSQL(connection, SqlHelper.getStringDeleteById(id, object));
    }

    public static <T> T getObjectById(Class classObject, long id) {
        try (Connection connection = ConnectionHolderPostgres.getConnection()) {
            return SqlHelper.getObjectById(connection, classObject, id);
        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*  public static void syncStructure(String[] packageForScan) {
          try (Connection c = ) {
              DataFromEntities dataFromEntities = scanTables(packageForScan);
              DataFromDatabase dataFromDatabase = scanDatabase(c);
              DataDiff diff = calculateDiff(dataFromEntities, dataFromDatabase);
              if (diff != null) {
                  diff.apply(c);
              }
          }*/
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, ClassNotFoundException {
      /* Entity entity28 = new Entity("кот", "Мфтроскин", "matroskin.com");
        insert(entity28);
        Class clazz = entity28.getClass().getSuperclass();
        Field field = null;

        field = clazz.getDeclaredField("id");
        field.setAccessible(true);
        String value = field.get(entity28).toString();
        System.out.println(value);

        Long id = entity28.getId();
        Entity entity = getObjectById(entity28.getClass(),27);*/
      // createMyTables("org.example.entity");
        syncStructure("org.example.entity");


    }
    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static void createTableInDataBase(Class[] classesWithPackage){
        for(int i = 0;i<= classesWithPackage.length-1;i++){
            createTable(classesWithPackage[i]);
        }

    }
    public static void createMyTables(String namePackage){
        try {
            createTableInDataBase(getClasses(namePackage));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void syncStructure(String packageName) throws IOException, ClassNotFoundException {
        Set<String> nameTable = SqlHelper.getSetTableWithDataBase();
        Class [] allNameClasses = getClasses(packageName);
        Set<String> nameClases = new LinkedHashSet<>();
        for(int i = 0;i<=allNameClasses.length-1;i++){
            nameClases.add(ParsingObject.getNameTable(allNameClasses[i]));
        }
        if(!nameClases.equals(nameTable)){
            createMyTables(packageName);
        }
    }
}

