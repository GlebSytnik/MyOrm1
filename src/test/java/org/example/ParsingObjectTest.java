package org.example;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ParsingObjectTest {
    private Entity entity;

    @Before
    public void setUp() {
        entity = new Entity(1, "Roma", "Det", "gmail");
    }

    @Test
    public void getNameTable() {
        String expected = ParsingObject.getNameTable(entity);
        String actual = "entity";
        assertEquals(expected, actual);

    }

    @Test
    public void getTypeObject() {
        String expected = ParsingObject.getTypeObject(String.class);
        String actual = "VARCHAR";
    }

    @Test
    public void getNameAndTypeField() {
        Map<String, Object> expected = ParsingObject.getNameAndTypeField(entity);
        Map<String, Object> actual = new LinkedHashMap<>();
        actual.put("id", Integer.class);
        actual.put("firstName", String.class);
        actual.put("lastName", String.class);
        actual.put("email", String.class);
        assertEquals(expected, actual);
    }

    @Test
    public void getNameAndValueField() {
        Map<String, String> expected = ParsingObject.getNameAndValueField(entity);
        Map<String, String> actual = new LinkedHashMap<>();
        actual.put("id", "1");
        actual.put("firstName", "Roma");
        actual.put("lastName", "Det");
        actual.put("email", "gmail");
        assertEquals(expected, actual);
    }

    @Test
    public void getValue() {
        Object expected = ParsingObject.getValue(entity, "id");
        Object actual = new Integer(1);
        assertEquals(expected, actual);
    }

    @Test
    public void createConcreteObject() {
    }

}