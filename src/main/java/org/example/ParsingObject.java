package org.example;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ParsingObject {

    public static String getNameObject(Object objectForTable){
        Class<? extends Object> classThisObject = objectForTable.getClass();
        String carClassName = classThisObject.getName();
        String[] words = carClassName.split("\\.");
        String className = words[words.length-1].toLowerCase(Locale.ROOT);
        return className;
    }

    public static Map<String,Object> getNameAndTypeField(Object objectForTable ){
        Class<? extends Object> classThisObject = objectForTable.getClass();
        Field[] declaredFields = classThisObject.getDeclaredFields();
        Map<String,Object> nameAndTypeObject = new HashMap<>();
        for (Field field : declaredFields) {
            nameAndTypeObject.put(field.getName(),field.getType());
        }
        return nameAndTypeObject;
    }

}
