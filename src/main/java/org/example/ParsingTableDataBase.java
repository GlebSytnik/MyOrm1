package org.example;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ParsingTableDataBase {

    public static String[] getColumnNames(ResultSetMetaData metadata) throws SQLException {
        int columnCount = metadata.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metadata.getColumnLabel(i + 1);
        }
        return columnNames;
    }

    public int getColumnCount(ResultSetMetaData metadata) throws SQLException {
        return metadata.getColumnCount();
    }
}
