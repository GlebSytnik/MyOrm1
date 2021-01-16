package org.example;

import org.apache.log4j.Logger;
import org.example.exception.BadConnectionExeception;
import org.example.exception.NotFieldException;
import org.example.exception.NotValueObjectException;
import org.example.exception.UnknownClassException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

public class ManagerOrm {

    final static Logger logger = Logger.getLogger(ManagerOrm.class);

    public static void createTable(Class object) {
        try {
            SqlHelper.executeSQL(SqlHelper.createTable(object));
        } catch (SQLException throwables) {
            logger.info("Create table don't exist ,check connection");
            throw new BadConnectionExeception("Bad connection in methods createTable");
        }
    }

    public static void dropTable(Class object) {
        SqlHelper.executeSQL(SqlHelper.dropTable(object));
    }

    public static <T> T insert(T objectClass) {
        Long id = SqlHelper.executeInsert(SqlHelper.getInsertSqlStringReturnId(objectClass, ParsingObject.getNameAndValueField(objectClass)));
        Class<?> classObjectParents = objectClass.getClass().getSuperclass();
        Field field;
        try {
            field = classObjectParents.getDeclaredField("id");
            field.setAccessible(true);
            field.set(objectClass, id);
            return (T) objectClass;
        } catch (Exception e) {
            logger.info("Create table don't exist ,check connection");
            throw new NotValueObjectException("This object does not value");
        }
    }

    public static void update(Object object, int id) {
        SqlHelper.executeSQL(SqlHelper.getSqlUpdate(id, object, ParsingObject.getNameAndValueField(object)));
    }

    public static void deleteById(int id, Object object) {
        SqlHelper.executeSQL(SqlHelper.getStringDeleteById(id, object));
    }

    public static <T> T getObjectById(Object objectForName, long id) {
        try {
            return SqlHelper.getObjectById(objectForName, id);
        } catch (IllegalAccessException e) {
            throw new NotFieldException("This field don't exist");
        }
    }

    public static void createTableInDataBase(Class[] classesWithPackage) {
        for (int i = 0; i <= classesWithPackage.length - 1; i++) {
            createTable(classesWithPackage[i]);
        }
    }

    public static void createMyTables(String namePackage) {
        createTableInDataBase(PackageHelper.getClasses(namePackage));
    }

    public static void syncStructure(String packageName) {
        Set<String> nameTable = SqlHelper.getSetTableWithDataBase();
        Class[] allNameClasses = PackageHelper.getClasses(packageName);
        Set<String> nameClasses = new LinkedHashSet<>();
        for (int i = 0; i <= allNameClasses.length - 1; i++) {
            nameClasses.add(ParsingObject.getNameTable(allNameClasses[i]));
        }
        if (!nameClasses.equals(nameTable)) {
            createMyTables(packageName);
        }
    }
}

