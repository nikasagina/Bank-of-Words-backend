CREATE DATABASE bank_of_words_test_db;
USE bank_of_words_test_db;

DROP TABLE IF EXISTS word_statistics;
DROP TABLE IF EXISTS known_words;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL
);

CREATE TABLE words (
                       word_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       word VARCHAR(255) NOT NULL,
                       definition TEXT NOT NULL,
                       creator_id BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE known_words (
                             user_id BIGINT NOT NULL,
                             word_id BIGINT NOT NULL,
                             FOREIGN KEY (user_id) REFERENCES users(user_id),
                             FOREIGN KEY (word_id) REFERENCES words(word_id),
                             PRIMARY KEY (user_id, word_id)
);

CREATE TABLE word_statistics (
                                 user_id BIGINT NOT NULL,
                                 word_id BIGINT NOT NULL,
                                 correct_count INTEGER NOT NULL,
                                 total_count INTEGER NOT NULL,
                                 FOREIGN KEY (user_id) REFERENCES users(user_id),
                                 FOREIGN KEY (word_id) REFERENCES words(word_id),
                                 PRIMARY KEY (user_id, word_id)
);



INSERT INTO words (word, definition) VALUES ('hello', 'Used as a greeting or to begin a phone conversation.');
INSERT INTO words (word, definition) VALUES ('world', 'The earth, together with all of its countries and peoples.');
INSERT INTO words (word, definition) VALUES ('computer', 'An electronic device capable of performing various tasks by executing pre-programmed instructions.');
INSERT INTO words (word, definition) VALUES ('programming', 'The process of designing, writing, testing, and maintaining the source code of computer programs.');
INSERT INTO words (word, definition) VALUES ('database', 'A structured set of data stored and accessed electronically.');
INSERT INTO words (word, definition) VALUES ('table', 'A collection of related data organized in rows and columns.');
INSERT INTO words (word, definition) VALUES ('query', 'A request for data or information from a database.');
INSERT INTO words (word, definition) VALUES ('algorithm', 'A set of step-by-step instructions for solving a specific problem or accomplishing a specific task.');
INSERT INTO words (word, definition) VALUES ('network', 'A collection of interconnected devices or computers that can communicate and share resources.');
INSERT INTO words (word, definition) VALUES ('encryption', 'The process of converting plaintext into ciphertext to secure it from unauthorized access.');