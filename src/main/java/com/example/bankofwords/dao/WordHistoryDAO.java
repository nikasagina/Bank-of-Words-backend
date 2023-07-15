package com.example.bankofwords.dao;

import com.example.bankofwords.objects.DailyUserReport;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class WordHistoryDAO {
    private final DataSource dataSource;
    private final UserDAO userDAO;

    public WordHistoryDAO(DataSource dataSource, UserDAO userDAO) {
        this.dataSource = dataSource;
        this.userDAO = userDAO;
    }

    public Map<Date, DailyUserReport> getFullDailyActivity(long userId) {
        Map<Date, DailyUserReport> reportMap = new HashMap<>();

        LocalDate joinDate = LocalDate.from(userDAO.getUserById(userId).getJoinDate());
        LocalDate currentDate = LocalDate.now();

        for (LocalDate date = joinDate; !date.isAfter(currentDate); date = date.plusDays(1)) {
            Date sqlDate = Date.valueOf(date);
            DailyUserReport dailyReport = getDailyActivity(userId, sqlDate);
            reportMap.put(sqlDate, dailyReport);
        }

        return reportMap;
    }


    public DailyUserReport getDailyActivity(long userId, Date date) {
        DailyUserReport dailyUserReport = new DailyUserReport(userId);
        String sql = "SELECT word_id, correct FROM word_history wh WHERE user_id = ? && DATE(answer_date) = DATE(?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setDate(2, date);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (resultSet.getBoolean("correct")) {
                        dailyUserReport.addCorrectAnswer();
                    } else {
                        dailyUserReport.addIncorrectAnswer();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dailyUserReport;
    }


    public void recordAnswer(long userId, long wordId, boolean correct) {
        String sql = "INSERT INTO word_history (user_id, word_id, correct) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, wordId);
            statement.setBoolean(3, correct);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
