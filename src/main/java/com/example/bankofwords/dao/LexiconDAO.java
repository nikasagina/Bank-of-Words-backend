package com.example.bankofwords.dao;

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
                resultSet.next();

                String definitionWithExample = resultSet.getString("gloss");

                int semicolonIndex = definitionWithExample.indexOf(";");
                if (semicolonIndex != -1)
                    return definitionWithExample.substring(0, semicolonIndex); // remove example
                return definitionWithExample;
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

}
