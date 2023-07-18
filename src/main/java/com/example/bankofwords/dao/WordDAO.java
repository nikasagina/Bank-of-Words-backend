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

    public Word getRandomWordFromTable(long tableId) {
        String sql = "SELECT word_id, word, definition, table_id FROM words w JOIN tables t USING(table_id) WHERE t.table_id = ? && definition != '' && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = t.creator_id) = 0 ORDER BY RAND() LIMIT 1;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, tableId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return new Word(resultSet.getLong(1), resultSet.getString(2),
                                    resultSet.getString(3), resultSet.getLong(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Word> getIncorrectWordsFromTable(Word correct, long tableId) {
        List<Word> result = new ArrayList<>();
        String sql = "SELECT DISTINCT word_id, word, definition, table_id FROM words w JOIN tables t USING(table_id) WHERE table_id = ? AND word != ? " +
                "&& (SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = t.creator_id) = 0 && definition != '' " +
                "ORDER BY RAND() LIMIT ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, tableId);
            statement.setString(2, correct.getWord());
            statement.setInt(3, NUMB_CORRECTS);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()){
                    result.add(new Word(resultSet.getLong(1), resultSet.getString(2),
                                        resultSet.getString(3), resultSet.getLong(4)));
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
        String sql = "SELECT word_id, word, definition, table_id FROM words WHERE word_id = ? ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, word_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return new Word(resultSet.getLong(1), resultSet.getString(2),
                                    resultSet.getString(3), resultSet.getLong(4));
            }
        } catch (SQLException e) {
            // Handle any exceptions
            e.printStackTrace();
        }

        return null;
    }

    public Long getWordId(String word, String definition, long user_id) {
        String sql = "SELECT word_id FROM words JOIN tables t USING(table_id) WHERE creator_id = ? && word = ? && definition = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setString(2, word);
            statement.setString(3, definition);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getLong(1);
            }
        } catch (SQLException e) {
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


    public void addWord(long tableId, String word, String definition) {
        String sql = "INSERT INTO words (word, definition, table_id) value (?, ?, ?);";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            statement.setString(2, definition);
            statement.setLong(3, tableId);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Word> getAllLearningWords(long userId) {
        String sql = "SELECT word_id, word, definition, table_id FROM words w JOIN tables t USING(table_id)  WHERE (creator_id = ? || creator_id = 1) && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) = 0 &&" +
                "(SELECT count(total_count) FROM word_statistics ws WHERE ws.user_id = ? && ws.word_id = w.word_id) > 0;";
        List<Word> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            statement.setLong(3, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    result.add(new Word(resultSet.getLong(1), resultSet.getString(2),
                                        resultSet.getString(3), resultSet.getLong(4)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Word> getAllLearnedWords(long userId) {
        String sql = "SELECT word_id, word, definition, table_id FROM words w JOIN tables t USING(table_id)  WHERE (creator_id = ? || creator_id = 1) && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) != 0;";
        List<Word> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setLong(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    result.add(new Word(resultSet.getLong(1), resultSet.getString(2),
                                        resultSet.getString(3), resultSet.getLong(4)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    // Random word that the user has already seen
    public Word getRandomWordWithProgressFromTable(long tableId) {
        String sql = "SELECT word_id, word, definition, table_id FROM words w JOIN tables t USING(table_id)  WHERE t.table_id = ? && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = t.creator_id) = 0 && " +
                "(SELECT COUNT(*) FROM word_statistics ws WHERE ws.word_id = w.word_id && user_id = t.creator_id) != 0  &&" +
                "definition != '' ORDER BY RAND() LIMIT 1;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, tableId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return new Word(resultSet.getLong(1), resultSet.getString(2),
                                    resultSet.getString(3), resultSet.getLong(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteWord(long wordId) {
        String sqlWords = "DELETE FROM words WHERE word_id = ?";
        String sqlStatistics = "DELETE FROM word_statistics WHERE word_id = ?";
        String sqlKnownWords = "DELETE FROM known_words WHERE word_id = ?";
        String sqlImages = "DELETE FROM word_images WHERE word_id = ?";
        String sqlHistory = "DELETE FROM word_history WHERE word_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statementWords = connection.prepareStatement(sqlWords);
             PreparedStatement statementStatistics = connection.prepareStatement(sqlStatistics);
             PreparedStatement statementKnownWords = connection.prepareStatement(sqlKnownWords);
             PreparedStatement statementImages = connection.prepareStatement(sqlImages);
             PreparedStatement statementHistory = connection.prepareStatement(sqlHistory)) {

            connection.setAutoCommit(false);

            statementWords.setLong(1, wordId);
            statementWords.executeUpdate();

            statementStatistics.setLong(1, wordId);
            statementStatistics.executeUpdate();

            statementKnownWords.setLong(1, wordId);
            statementKnownWords.executeUpdate();

            statementImages.setLong(1, wordId);
            statementImages.executeUpdate();

            statementHistory.setLong(1, wordId);
            statementHistory.executeUpdate();

            connection.commit();  // Commit the changes

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Word> getTableWords(long tableId) {
        String sql = "SELECT word_id, word, definition, table_id FROM words w JOIN tables t USING(table_id)  WHERE t.table_id = ?;";
        List<Word> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, tableId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    result.add(new Word(resultSet.getLong(1), resultSet.getString(2),
                            resultSet.getString(3), resultSet.getLong(4)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
