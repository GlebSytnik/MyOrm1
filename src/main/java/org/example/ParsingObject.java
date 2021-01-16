package org.example;

import org.example.exception.NotNameAndTypeFieldException;
import org.example.exception.NotValueObjectException;
import org.example.exception.UnknownObjectTypeExeception;

import java.lang.reflect.Field;
import java.util.*;

public class ParsingObject {

    public static String getNameTable(Object objectForTable) {
        String name = objectForTable.getClass().getSimpleName().toLowerCase();
        return name;
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
            } else if (objectForTable.equals(Long.class)) {
                resultclassName = "INTEGER";
                return resultclassName;
            } else {
                throw new UnknownObjectTypeExeception("Unknown current type object");
            }
        } catch (UnknownObjectTypeExeception unknownObjectTypeExeception) {
            throw new UnknownObjectTypeExeception("Unknown current type object");
        }
    }

    public static Map<String, Object> getNameAndTypeField(Class objectForTable) {
        Field[] declaredFields = objectForTable.getDeclaredFields();
        Map<String, Object> nameAndTypeObject = new LinkedHashMap<>();
        for (Field field : declaredFields) {
            nameAndTypeObject.put(field.getName(), field.getType());
        }
        if (nameAndTypeObject == null) {
            throw new NotNameAndTypeFieldException("This object has no name or type");
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
            throw new NotValueObjectException("This object does not value");
        }
    }
}
