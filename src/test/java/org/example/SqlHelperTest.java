package org.example;

import org.example.db.ConnectionHolderPostgres;
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
        entity = new Entity( "Roma", "Det", "gmail");
        nameAndValueField = new LinkedHashMap<>();
        nameAndValueField.put("id", "1");
        nameAndValueField.put("firstName", "Roma");
        nameAndValueField.put("lastName", "Det");
        nameAndValueField.put("email", "gmail");

        nameAndTypeField = new LinkedHashMap<>();
        nameAndTypeField.put("id", Integer.class);
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
        String actual = "create table if not exists entity ( id INTEGER,firstName VARCHAR,lastName VARCHAR,email VARCHAR );";
        assertEquals(expected, actual);
    }

    @Test
    public void getSqlQueryCreate() {
        String expected = SqlHelper.getSqlQueryCreate(nameAndTypeField, entity.getClass());
        String actual = "create table if not exists entity ( id INTEGER,firstName VARCHAR,lastName VARCHAR,email VARCHAR );";
        assertEquals(expected, actual);
    }

    @Test
    public void getInsertSql() {
        String expected = SqlHelper.getInsertSqlStringReturnId(entity, nameAndValueField);
        String actual = "insert into  entity (id ,firstName ,lastName ,email ) VALUES ( '1' ,'Roma' ,'Det' ,'gmail'  );";
        assertEquals(expected, actual);
    }

    @Test
    public void getInsertFieldsNameSqlBuilder() {
        String expected = SqlHelper.getInsertFieldsNameSqlBuilder(nameAndValueField);
        String actual = "id ,firstName ,lastName ,email ";
        assertEquals(expected, actual);
    }

    @Test
    public void getInsertFieldsValueSqlBuilder() {
        String expected = SqlHelper.getInsertFieldsValueSqlBuilder(nameAndValueField);
        String actual = "'1' ,'Roma' ,'Det' ,'gmail' ";
        assertEquals(expected, actual);
    }

    @Test
    public void getSqlUpdate() {
        String expected = SqlHelper.getSqlUpdate(1, entity.getClass(), nameAndValueField);
        String actual = "update entity  set  id = '1' ,firstName = 'Roma' ,lastName = 'Det' ,email = 'gmail'   WHERE id = 1 ;";
        assertEquals(expected, actual);
    }

    @Test
    public void getFieldAndValueSqlUpdate() {
        String expected = SqlHelper.getFieldAndValueSqlUpdate(nameAndValueField);
        String actual = "id = '1' ,firstName = 'Roma' ,lastName = 'Det' ,email = 'gmail' ";
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
        actual.add(1, "Roma");
        actual.add(2, "Las");
        actual.add(3, "gmail");
        assertEquals(expected, actual);
    }

    @Test
    public void createConcreteObject() {
        Entity expectedEntity = SqlHelper.createConcreteObject(entity.getClass());
        expectedEntity.setId(1L);
        expectedEntity.setFirstName("Roma");
        expectedEntity.setLastName("Det");
        expectedEntity.setEmail("gmail");
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
        actual.setId(1L);
        actual.setFirstName("Roma");
        actual.setLastName("Las");
        actual.setEmail("gmail");
        assertTrue(expectedEntity.equals(actual));
    }
}