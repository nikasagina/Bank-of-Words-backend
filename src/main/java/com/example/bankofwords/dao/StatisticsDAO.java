package com.example.bankofwords.dao;

import com.example.bankofwords.objects.Word;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsDAO {
    private final DataSource dataSource;

    public StatisticsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void incrementTotal(long user_id, long word_id) {
        String sql = "INSERT INTO word_statistics (user_id, word_id, correct_count, total_count) " +
                "VALUES (?, ?, 0, 1) ON DUPLICATE KEY UPDATE total_count = total_count + 1;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, word_id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementCorrectAndTotal(long user_id, long word_id) {
        String sql = "INSERT INTO word_statistics (user_id, word_id, correct_count, total_count) " +
                "VALUES (?, ?, 1, 1) ON DUPLICATE KEY UPDATE correct_count = correct_count + 1, total_count = total_count + 1;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, word_id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTotalCount(long user_id, long word_id) {
        String sql = "SELECT total_count FROM word_statistics WHERE user_id = ? && word_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, word_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return 0;
    }

    public Double getSuccessRate(long user_id, long word_id) {
        String sql = "SELECT correct_count, total_count FROM word_statistics WHERE user_id = ? AND word_id = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, word_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int correctCount = resultSet.getInt(1);
                    int totalCount = resultSet.getInt(2);
                    if (totalCount > 0) {
                        return (double) correctCount / totalCount;
                    }
                }
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return null;
    }

}
