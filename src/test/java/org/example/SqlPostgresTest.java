package org.example;

import org.example.db.ConnectionHolderPostgres;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class SqlPostgresTest {

    private Entity entity;
    private Map<String, String> nameAndValueField;

    @Before
    public void setUp() {
        entity = new Entity(1, "Roma", "Det", "gmail");
        nameAndValueField = new LinkedHashMap<>();
        nameAndValueField.put("id", "1");
        nameAndValueField.put("firstName", "Roma");
        nameAndValueField.put("lastName", "Det");
        nameAndValueField.put("email", "gmail");
    }

    @Test
    public void executeSQL() {
    }

    @Test
    public void createTable() throws SQLException {
        String expected = SqlPostgres.createTable(entity);
        String actual = "create table if not exists entity ( id INTEGER,firstName VARCHAR,lastName VARCHAR,email VARCHAR );";
        assertEquals(expected, actual);

    }

    @Test
    public void getSqlQueryCreate() {
    }

    @Test
    public void getInsertSql() {
        String expected = SqlPostgres.getInsertSql(entity, nameAndValueField);
        String actual = "insert into  entity (id ,firstName ,lastName ,email ) VALUES ( '1' ,'Roma' ,'Det' ,'gmail'  );";
        assertEquals(expected, actual);
    }

    @Test
    public void getInsertFieldsNameSqlBuilder() {
        String expected = SqlPostgres.getInsertFieldsNameSqlBuilder(nameAndValueField);
        String actual = "'1' ,'Roma' ,'Det' ,'gmail' ";
    }

    @Test
    public void getInsertFieldsValueSqlBuilder() {
    }

    @Test
    public void getSqlUpdate() {
        String expected = SqlPostgres.getSqlUpdate(1, entity, nameAndValueField);
        String actual = "update entity  set  id = '1' ,firstName = 'Roma' ,lastName = 'Det' ,email = 'gmail'   WHERE id = 1 ;";
        assertEquals(expected, actual);
    }

    @Test
    public void getFieldAndValueSqlUpdate() {
        String expected = SqlPostgres.getFieldAndValueSqlUpdate(nameAndValueField);
        String actual = "id = '1' ,firstName = 'Roma' ,lastName = 'Det' ,email = 'gmail' ";
        assertEquals(expected, actual);
    }

    @Test
    public void getStringDeleteById() {
        String expected = SqlPostgres.getStringDeleteById(1, entity);
        String actual = "DELETE FROM entity WHERE id = 1";
        assertEquals(expected, actual);
    }

    @Test
    public void getStringFindById() {
        String expected = SqlPostgres.getStringFindById(1, entity);
        String actual = "SELECT * FROM entity WHERE id = 1";
        assertEquals(expected, actual);
    }

    @Test
    public void getSetNameBase() {
        Set<String> expected = SqlPostgres.getSetNameBase(ConnectionHolderPostgres.getConnection(), entity, 1);
        Set<String> actual = new LinkedHashSet<>();
        actual.add("id");
        actual.add("firstname");
        actual.add("lastname");
        actual.add("email");
        assertEquals(expected, actual);
    }

    @Test
    public void getListValueBase() {
        List<Object> expected = SqlPostgres.getListValueBase(ConnectionHolderPostgres.getConnection(), entity, 1);
        List<Object> actual = new ArrayList<>();
        actual.add(0, new Integer(1));
        actual.add(1, new String("Roma"));
        actual.add(2, new String("Las"));
        actual.add(3, new String("gmail"));
        assertEquals(expected, actual);
    }

    @Test
    public void createConcreteObject() throws InstantiationException, IllegalAccessException {
        Object expectedEntity = SqlPostgres.createConcreteObject(entity);
        Object actual = expectedEntity;
        assertEquals(expectedEntity, actual);
    }

    @Test
    public void getObjectById() {

    }

}