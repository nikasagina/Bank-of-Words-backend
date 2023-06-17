package com.example.bankofwords.dao;


import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public Double getUserSuccessRateForWord(long user_id, long word_id) {
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

    public int getUserTotalGuessesCount(long user_id) {
        String sql = "SELECT SUM(total_count) FROM word_statistics WHERE user_id = ?;";
        return getUserStatisticsHelper(user_id, sql);
    }

    public int getUserSuccessfulGuessesCount(long user_id){
        String sql = "SELECT SUM(correct_count) FROM word_statistics WHERE user_id = ?;";
        return getUserStatisticsHelper(user_id, sql);
    }

    private int getUserStatisticsHelper(long user_id, String sql) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return 0;
    }

    public Double getUserSuccessRateTotal(long user_id) {
        double total = getUserTotalGuessesCount(user_id);
        double correct = getUserSuccessfulGuessesCount(user_id);
        return correct / total;
    }

    public long getMostGuessedWordByUser(long user_id) {
        String sql = "SELECT word_id FROM word_statistics WHERE user_id = ? ORDER BY correct_count / total_count DESC LIMIT 1;";
        return getGuessedWordByUserHelper(user_id, sql);
    }

    public long getLeastGuessedWordByUser(long user_id) {
        String sql = "SELECT word_id FROM word_statistics WHERE user_id = ? ORDER BY correct_count / total_count LIMIT 1;";
        return getGuessedWordByUserHelper(user_id, sql);
    }

    private long getGuessedWordByUserHelper(long user_id, String sql) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next())
                    return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long getMostGuessedWordOverall() {
        String sql = "SELECT word_id FROM word_statistics ORDER BY correct_count / total_count DESC LIMIT 1;";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next())
                    return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long getLeastGuessedWordOverall() {
        String sql = "SELECT word_id FROM word_statistics ORDER BY correct_count / total_count LIMIT 1;";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            try (ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next())
                    return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<Long> getTopUserIdsWithBestSuccessRate(int num) {
        List<Long> result = new ArrayList<>();
        String sql = "SELECT user_id, SUM(correct_count) / SUM(total_count) AS max FROM word_statistics" +
                    " GROUP BY user_id ORDER BY max DESC LIMIT ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, num);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(resultSet.getLong(1));
                }

                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
