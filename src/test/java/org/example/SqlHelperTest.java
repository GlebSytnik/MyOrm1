package org.example;

import org.example.entity.Entity;
import org.example.exception.NotFieldException;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class SqlHelperTest {

    private Entity entity;
    private Map<String, String> nameAndValueField;
    private Map<String, Object> nameAndTypeField;

    @Before
    public void setUp() {
        entity = new Entity( "Матроскин", "Кот", "matroskin.com");
        nameAndValueField = new LinkedHashMap<>();
        nameAndValueField.put("firstName", "Кот");
        nameAndValueField.put("lastName", "Матроскин");
        nameAndValueField.put("email", "matroskin.com");

        nameAndTypeField = new LinkedHashMap<>();
        nameAndTypeField.put("firstName", String.class);
        nameAndTypeField.put("lastName", String.class);
        nameAndTypeField.put("email", String.class);
    }

    @Test
    public void executeSQL() throws SQLException {
        assertTrue(SqlHelper.executeSQL("SELECT * FROM entity"));
    }

    @Test
    public void createTable() throws SQLException {
        String expected = SqlHelper.createTable(entity.getClass());
        String actual = "create table if not exists  entity ( id SERIAL ,firstName VARCHAR,lastName VARCHAR,email VARCHAR ) ;";
        assertEquals(expected, actual);
    }

    @Test
    public void getSqlQueryCreate() {
        String expected = SqlHelper.getSqlQueryCreate(nameAndTypeField, entity.getClass());
        String actual = "create table if not exists  entity ( id SERIAL ,firstName VARCHAR,lastName VARCHAR,email VARCHAR ) ;";
        assertEquals(expected, actual);
    }

    @Test
    public void getInsertSql() {
        String expected = SqlHelper.getInsertSqlStringReturnId(entity.getClass(), nameAndValueField);
        String actual = "insert into  entity (firstName ,lastName ,email ) VALUES ( 'Кот' ,'Матроскин' ,'matroskin.com'  ) RETURNING id;";
        assertEquals(expected, actual);
    }

    @Test
    public void getInsertFieldsNameSqlBuilder() {
        String expected = SqlHelper.getInsertFieldsNameSqlBuilder(nameAndValueField);
        String actual = "firstName ,lastName ,email ";
        assertEquals(expected, actual);
    }

    @Test
    public void getInsertFieldsValueSqlBuilder() {
        String expected = SqlHelper.getInsertFieldsValueSqlBuilder(nameAndValueField);
        String actual = "'Кот' ,'Матроскин' ,'matroskin.com' ";
        assertEquals(expected, actual);
    }

    @Test
    public void getSqlUpdate() {
        String expected = SqlHelper.getSqlUpdate(1, entity, nameAndValueField);
        String actual = "update entity  set  firstName = 'Кот' ,lastName = 'Матроскин' ,email = 'matroskin.com'   WHERE id = 1 ;";
        assertEquals(expected, actual);
    }

    @Test
    public void getFieldAndValueSqlUpdate() {
        String expected = SqlHelper.getFieldAndValueSqlUpdate(nameAndValueField);
        String actual = "firstName = 'Кот' ,lastName = 'Матроскин' ,email = 'matroskin.com' ";
        assertEquals(expected, actual);
    }

    @Test
    public void getStringDeleteById() {
        String expected = SqlHelper.getStringDeleteById(1, entity);
        String actual = "DELETE FROM entity WHERE id = 1";
        assertEquals(expected, actual);
    }

    @Test
    public void getStringFindById() {
        String expected = SqlHelper.getStringFindById(1, entity);
        String actual = "SELECT * FROM entity WHERE id = 1";
        assertEquals(expected, actual);
    }

    @Test
    public void getSetNameBase() throws SQLException {
        Set<String> expected = SqlHelper.getSetNameBase( entity, 1);
        Set<String> actual = new LinkedHashSet<>();
        actual.add("id");
        actual.add("firstname");
        actual.add("lastname");
        actual.add("email");
        assertEquals(expected, actual);
    }

    @Test
    public void getListValueBase() throws SQLException {
        List<Object> expected = SqlHelper.getListValueBase(entity, 1);
        List<Object> actual = new ArrayList<>();
        actual.add(0, 1);
        actual.add(1, "Кот");
        actual.add(2, "Матроскин");
        actual.add(3, "matroskin.com");
        assertEquals(expected, actual);
    }

    @Test
    public void createConcreteObject() {
        Entity expectedEntity = SqlHelper.createConcreteObject(entity.getClass());
        expectedEntity.setId(1L);
        expectedEntity.setFirstName("Кот");
        expectedEntity.setLastName("Матроскин");
        expectedEntity.setEmail("matroskin.com");
        Entity actual = entity;
        assertTrue(expectedEntity.equals(actual));
    }

    @Test
    public void getObjectById() {
        Entity expectedEntity = null;
        try {
            expectedEntity = SqlHelper.getObjectById(entity, 1);
        } catch (IllegalAccessException e) {
           throw new NotFieldException("This field don't exist");
        }
        Entity actual = new Entity();
        actual.setFirstName("Кот");
        actual.setLastName("Матроскин");
        actual.setEmail("matroskin.com");
        assertTrue(expectedEntity.equals(actual));
    }
}