package com.example.bankofwords.dao;

import com.example.bankofwords.utils.WordUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LexiconDAO {
    private final DataSource dataSource;

    public LexiconDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getWordDefinition(String word) {
        String sql = "SELECT w.word, g.gloss FROM wn_synset AS w JOIN wn_gloss AS g ON w.synset_id = g.synset_id WHERE w.word = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {

                    String definitionWithExample = resultSet.getString("gloss");

                    int semicolonIndex = definitionWithExample.indexOf(";");
                    if (semicolonIndex != -1)
                        return definitionWithExample.substring(0, semicolonIndex); // remove example
                    return definitionWithExample;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public boolean contains(String word) {
        String sql = "SELECT COUNT(*) AS count FROM wn_synset WHERE word = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                int count = resultSet.getInt("count");
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getDefinitions(String word) {
        String sql = "SELECT w.word, g.gloss FROM wn_synset AS w JOIN wn_gloss AS g ON w.synset_id = g.synset_id WHERE w.word = ?;";
        List<String> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                while(resultSet.next()) {
                    String definitionWithExample = resultSet.getString("gloss");
                    int semicolonIndex = definitionWithExample.indexOf(";");
                    if (semicolonIndex != -1)
                        result.add(definitionWithExample.substring(0, semicolonIndex));
                    else result.add(definitionWithExample);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<String> getDefinitionExamples(String word, String definition) {
        String sql = "SELECT w.word, g.gloss FROM wn_synset AS w JOIN wn_gloss AS g ON w.synset_id = g.synset_id WHERE w.word = ? AND g.gloss LIKE ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            statement.setString(2, definition + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()) {
                    String definitionWithExample = resultSet.getString("gloss");
                    return WordUtil.extractStringsInQuotes(definitionWithExample);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getWordSynonyms(String word) {
        String sql = "SELECT DISTINCT w2.word FROM wn_synset AS w1 " +
                "JOIN wn_synset AS w2 ON w1.synset_id = w2.synset_id " +
                "WHERE w1.word = ? AND w1.w_num = 1 AND w2.w_num > 1;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<String> synonyms = new ArrayList<>();
                while (resultSet.next()) {
                    synonyms.add(resultSet.getString("word").replace("_", " "));
                }
                return synonyms;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getWordAntonyms(String word) {
        String sql = "SELECT DISTINCT w2.word FROM wn_synset AS w1 " +
                "JOIN wn_antonym AS a ON w1.synset_id = a.synset_id_1 " +
                "JOIN wn_synset AS w2 ON a.synset_id_2 = w2.synset_id " +
                "WHERE w1.word = ?;";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, word);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<String> antonyms = new ArrayList<>();
                while (resultSet.next()) {
                    antonyms.add(resultSet.getString("word").replace("_", " "));
                }
                return antonyms;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
