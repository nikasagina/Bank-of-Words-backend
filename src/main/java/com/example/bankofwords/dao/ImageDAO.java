package com.example.bankofwords.dao;

import com.example.bankofwords.objects.Image;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ImageDAO {
    private final DataSource dataSource;


    public ImageDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Image getRandomImage(long user_id) {
        String sql = "SELECT word_id, image_name FROM word_images wi join words w using(word_id) join tables t using(table_id)" +
                " WHERE (creator_id = ? || creator_id = 0) && " +
                "(SELECT COUNT(*) FROM known_words kw WHERE kw.word_id = w.word_id && user_id = ?) = 0 ORDER BY RAND() LIMIT 1;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user_id);
            statement.setLong(2, user_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return new Image(resultSet.getLong("word_id"), resultSet.getString("image_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addImage(long word_id, String imageName) {
        String sql = "INSERT INTO word_images (word_id, image_name) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, word_id);
            statement.setString(2, imageName);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Image getRandomImageFromTable(long userID) {
        return null;
    }
}
