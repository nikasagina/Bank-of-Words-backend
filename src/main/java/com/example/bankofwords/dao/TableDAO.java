package com.example.bankofwords.dao;

import com.example.bankofwords.objects.Table;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    private final DataSource dataSource;

    public TableDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createTable(long userId, String tableName) {
        String sql = "INSERT INTO tables (creator_id, table_name) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, tableName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTable(long tableId) {
        String deleteWordsSQL = "DELETE FROM words WHERE table_id = ?";
        String deleteTableSQL = "DELETE FROM tables WHERE table_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deleteWordsStatement = connection.prepareStatement(deleteWordsSQL);
             PreparedStatement deleteTableStatement = connection.prepareStatement(deleteTableSQL)) {
            deleteWordsStatement.setLong(1, tableId);
            deleteWordsStatement.execute();

            deleteTableStatement.setLong(1, tableId);
            deleteTableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Long getTableId(long userId, String tableName) {
        String sql = "SELECT table_id FROM tables WHERE creator_id = ? AND table_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Table> getInitialTables() {
        return getUserTables(0);
    }

    public List<Table> getUserTables(long userId) {
        String sql = "SELECT * FROM tables WHERE creator_id = ?";
        List<Table> tables = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    tables.add(new Table(resultSet.getLong("table_id"),
                            resultSet.getLong("creator_id"), resultSet.getString("table_name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tables;
    }


    public Boolean containsWord(long tableId, String word) {
        String sql = "SELECT word_id FROM words JOIN tables t USING(table_id) WHERE word = ? AND table_id = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            statement.setLong(2, tableId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean existsTable(long userId, String tableName) {
        String sql = "SELECT * FROM tables WHERE creator_id = ? AND table_name = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, tableName);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
