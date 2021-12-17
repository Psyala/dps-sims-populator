package com.psyala.db;

import java.sql.*;
import java.util.Optional;

public class SQLiteConnection {
    private final String fullPath;

    private Connection connection = null;

    public SQLiteConnection(String fullPath) {
        this.fullPath = fullPath;
    }

    private void connect() throws SQLException {
        String url = "jdbc:sqlite:" + fullPath;
        connection = DriverManager.getConnection(url);
        System.out.println("Connection established.");
    }

    private void disconnect() throws SQLException {
        if (connection == null) return;
        connection.close();
        connection = null;
    }

    public void delete(String sql) {
        insert(sql);
    }

    public int insert(String sql) {
        int returnId = -1;
        try {
            connect();

            Statement statement = connection.createStatement();
            returnId = statement.executeUpdate(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return returnId;
    }

    public Optional<ResultSet> select(String sql) {
        try {
            connect();

            Statement statement = connection.createStatement();
            return Optional.ofNullable(statement.executeQuery(sql));
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                disconnect();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return Optional.empty();
    }

}
