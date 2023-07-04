package com.example.bankofwords.dao;

import com.example.bankofwords.objects.Word;
import org.jetbrains.annotations.NotNull;

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


    public Word getRandomWord(long user_id) {
        String sql = "SELECT word, definition FROM words w WHERE (creator_id = ? || creator_id = 0) && definition != '' && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) = 0 ORDER BY RAND() LIMIT 1;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                return new Word(resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Word> getIncorrectWords(Word correct, long user_id) {
        List<Word> result = new ArrayList<>();
        String sql = "SELECT word, definition FROM words w WHERE creator_id = ? || creator_id = 0 AND word != ? " +
                "&& definition != '' && (SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) = 0 " +
                "ORDER BY RAND() LIMIT ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setString(2, correct.getWord());
            statement.setLong(3, user_id);
            statement.setInt(4, NUMB_CORRECTS);
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

    public Word getWordWithId(long word_id) {
        if (word_id == 0) return null;
        String sql = "SELECT word, definition FROM words WHERE word_id = ? ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, word_id);
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

    public Long getWordId(String word, long user_id) {
        Long userId = getWordIdWithCreator(word, user_id);
        if (userId != null) {
            return user_id;
        }
        return getWordIdWithCreator(word, 0);
    }

    private Long getWordIdWithCreator(String word, long user_id) {
        String sql = "SELECT word_id FROM words WHERE creator_id = ? && word = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setString(2, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return null;
    }

    public boolean alreadyKnows(long user_id, long word_id) {
        String sql = "SELECT COUNT(*) FROM known_words WHERE user_id = ? && word_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, word_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();

                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return false;
    }

    public void learnWord(long user_id, long word_id) {
        String sql = "INSERT INTO known_words (user_id, word_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, word_id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Boolean userWordsContain(long user_id, String word) {
        String sql = "SELECT word_id FROM words WHERE word = ? AND (creator_id = 0 || creator_id = ?);";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            statement.setLong(2, user_id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void addWord(long userId, String word, String definition) {
        String sql = "INSERT INTO words (word, definition, creator_id) value (?, ?, ?);";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            statement.setString(2, definition);
            statement.setLong(3, userId);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Word> getAllLearningWords(long userId) {
        String sql = "SELECT word, definition FROM words w WHERE (creator_id = ? || creator_id = 0) && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) = 0;";
        return getWordList(userId, sql);
    }

    public List<Word> getAllLearnedWords(long userId) {
        String sql = "SELECT word, definition FROM words w WHERE (creator_id = ? || creator_id = 0) && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) != 0;";
        return getWordList(userId, sql);
    }

    private List<Word> getWordList(long userId, String sql) {
        List<Word> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    result.add(new Word(resultSet.getString(1), resultSet.getString(2)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Random word that the user has already seen
    public Word getRandomWordWithProgress(long user_id) {
        String sql = "SELECT word, definition FROM words w WHERE (creator_id = ? || creator_id = 0) && definition != '' && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) = 0 && " +
                "(SELECT COUNT(*) FROM word_statistics ws WHERE ws.word_id = w.word_id && user_id = ?) != 0 " +
                "ORDER BY RAND() LIMIT 1;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, user_id);
            statement.setLong(3, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return new Word(resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
