package com.example.bankofwords.dao;

import com.example.bankofwords.objects.Word;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class WordDAO {
    public static final int NUMB_CORRECTS = 3;

    private final DataSource dataSource;

    public WordDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Word getRandomWord(int user_id) {
        String sql = "SELECT word, definition FROM initial_words UNION" +
                " SELECT word, definition FROM added_words WHERE creator_id = ? ORDER BY RAND() LIMIT 1";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                return new Word(resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
        return null;
    }


    public List<Word> getIncorrectWords(Word correct, int user_id) {
        List<Word> result = new ArrayList<>();
        String sql = "SELECT word, definition FROM initial_words UNION" +
                " SELECT word, definition FROM added_words WHERE creator_id = ? AND word != ? ORDER BY RAND() LIMIT ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, user_id);
            statement.setString(2, correct.getWord());
            statement.setInt(3, NUMB_CORRECTS);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()){
                    result.add(new Word(resultSet.getString(1), resultSet.getString(2)));
                }

                return result;
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
        return null;
    }
}
