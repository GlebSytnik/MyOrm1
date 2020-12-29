package org.example;

import org.example.exception.UnknownObjectTypeExeception;
import java.lang.reflect.Field;
import java.util.*;

public class ParsingObject {

    public static String getNameTable(Object objectForTable) {
        Class<? extends Object> classThisObject = objectForTable.getClass();
        String getAllClassName = classThisObject.getName();
        String[] words = getAllClassName.split("\\.");
        String className = words[words.length - 1].toLowerCase(Locale.ROOT);
        return className;
    }

    public static String getTypeObject(Object objectForTable) {
        try {
            String resultclassName = " ";
            if (objectForTable.equals(String.class)) {
                resultclassName = "VARCHAR";
                return resultclassName;
            } else if (objectForTable.equals(Integer.class)) {
                resultclassName = "INTEGER";
                return resultclassName;
            } else {

                throw new UnknownObjectTypeExeception("Unknown current type object");
            }
        } catch (UnknownObjectTypeExeception unknownObjectTypeExeception) {
            unknownObjectTypeExeception.printStackTrace();
        }
        return "";
    }

    public static Map<String, Object> getNameAndTypeField(Object objectForTable) {
        Class<? extends Object> classThisObject = objectForTable.getClass();
        Field[] declaredFields = classThisObject.getDeclaredFields();
        Map<String, Object> nameAndTypeObject = new LinkedHashMap<>();
        for (Field field : declaredFields) {
            nameAndTypeObject.put(field.getName(), field.getType());
        }
        return nameAndTypeObject;
    }

    public static Map<String, String> getNameAndValueField(Object objectForTable) {
        Class<? extends Object> classThisObject = objectForTable.getClass();
        Field[] declaredFields = classThisObject.getDeclaredFields();
        List<String> listName = new ArrayList<>();
        List<String> listValue = new ArrayList<>();
        Map<String, String> nameAndValueField = new LinkedHashMap<>();

        for (Field field : declaredFields) {
            listName.add(field.getName());
        }
        for (String name : listName) {
            listValue.add(getValue(objectForTable, name).toString());
        }
        for (int i = 0; i <= listName.size() - 1; i++) {
            nameAndValueField.put(listName.get(i), listValue.get(i));
        }
        return nameAndValueField;
    }

    public static Object getValue(Object object, String fieldName) {
        if (object == null) {
            return null;
        }
        Field field = null;
        Class<?> classObject = object.getClass();
        try {
            field = classObject.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.toString();
        }
        return object;
    }

    static Object createConcreteObject (Object object){
        Class clazz = object.getClass();
        Object newObject = null;
        try {
            newObject = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return newObject;
    }

}
