package com.example.bankofwords.unittests;

import com.example.bankofwords.ConstantsForTests;
import com.example.bankofwords.dao.UserDAO;
import org.apache.commons.dbcp2.BasicDataSource;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TestUserDAO extends TestCase {

    private UserDAO dao;

    @Override
    protected void setUp() throws Exception {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(ConstantsForTests.TESTING_DB_CLASS_NAME);
            dataSource.setUrl(ConstantsForTests.TESTING_DB_URL);
            dataSource.setUsername(ConstantsForTests.TESTING_DB_USERNAME);
            dataSource.setPassword(ConstantsForTests.TESTING_DB_PASSWORD);

            dao = new UserDAO(dataSource);

            populateDatabase(dataSource);

        } catch (Exception e) {
            throw new RuntimeException("Database connection error");
        }
    }

    private void populateDatabase(BasicDataSource dataSource) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            // Create the database
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS bank_of_words_test_db");
            statement.executeUpdate("USE bank_of_words_test_db");

            // Drop existing tables
            statement.executeUpdate("DROP TABLE IF EXISTS word_statistics");
            statement.executeUpdate("DROP TABLE IF EXISTS known_words");
            statement.executeUpdate("DROP TABLE IF EXISTS words");
            statement.executeUpdate("DROP TABLE IF EXISTS users");

            // Create the users table
            statement.executeUpdate("CREATE TABLE users (user_id BIGINT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) NOT NULL, password_hash VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL)");

            // Create the words table
            statement.executeUpdate("CREATE TABLE words (word_id BIGINT AUTO_INCREMENT PRIMARY KEY, word VARCHAR(255) NOT NULL, definition TEXT NOT NULL, creator_id BIGINT NOT NULL DEFAULT 0)");

            // Create the known_words table
            statement.executeUpdate("CREATE TABLE known_words (user_id BIGINT NOT NULL, word_id BIGINT NOT NULL, FOREIGN KEY (user_id) REFERENCES users(user_id), FOREIGN KEY (word_id) REFERENCES words(word_id), PRIMARY KEY (user_id, word_id))");

            // Create the word_statistics table
            statement.executeUpdate("CREATE TABLE word_statistics (user_id BIGINT NOT NULL, word_id BIGINT NOT NULL, correct_count INTEGER NOT NULL, total_count INTEGER NOT NULL, FOREIGN KEY (user_id) REFERENCES users(user_id), FOREIGN KEY (word_id) REFERENCES words(word_id), PRIMARY KEY (user_id, word_id))");

            // Insert data into the words table
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('hello', 'Used as a greeting or to begin a phone conversation.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('world', 'The earth, together with all of its countries and peoples.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('computer', 'An electronic device capable of performing various tasks by executing pre-programmed instructions.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('programming', 'The process of designing, writing, testing, and maintaining the source code of computer programs.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('database', 'A structured set of data stored and accessed electronically.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('table', 'A collection of related data organized in rows and columns.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('query', 'A request for data or information from a database.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('algorithm', 'A set of step-by-step instructions for solving a specific problem or accomplishing a specific task.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('network', 'A collection of interconnected devices or computers that can communicate and share resources.')");
            statement.executeUpdate("INSERT INTO words (word, definition) VALUES ('encryption', 'The process of converting plaintext into ciphertext to secure it from unauthorized access.')");

        } catch (SQLException e) {
            throw new RuntimeException("Error populating the database", e);
        }
    }


    public void testName() {
    }
}