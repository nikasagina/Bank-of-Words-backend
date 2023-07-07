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
}
