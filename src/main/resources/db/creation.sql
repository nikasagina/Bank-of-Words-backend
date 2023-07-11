CREATE DATABASE IF NOT EXISTS bank_of_words_db;
USE bank_of_words_db;


DROP TABLE IF EXISTS word_statistics;
DROP TABLE IF EXISTS known_words;
DROP TABLE IF EXISTS word_images;
DROP TABLE IF EXISTS word_history;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS tables;

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) AUTO_INCREMENT = 1;

CREATE TABLE tables (
                        table_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        creator_id INT,
                        table_name VARCHAR(255)
);

CREATE TABLE words (
                       word_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       word VARCHAR(255) NOT NULL,
                       definition TEXT NOT NULL,
                       table_id BIGINT NOT NULL DEFAULT 1
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

CREATE TABLE word_images (
                             image_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             word_id BIGINT NOT NULL,
                             image_name VARCHAR(255) NOT NULL,
                             FOREIGN KEY (word_id) REFERENCES words(word_id)
);

CREATE TABLE word_history (
                              user_id BIGINT NOT NULL,
                              word_id BIGINT NOT NULL,
                              correct BOOLEAN NOT NULL,
                              answer_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES users(user_id),
                              FOREIGN KEY (word_id) REFERENCES words(word_id),
                              PRIMARY KEY (user_id, word_id, answer_date)
);


insert into users (user_id, username, password_hash, email) VALUES (1, 'admin', '5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5', '');

INSERT INTO tables (table_id, creator_id, table_name) VALUES (1, 1, 'initial-simple');
INSERT INTO tables (table_id, creator_id, table_name) VALUES (2, 1, 'initial-medium');
INSERT INTO tables (table_id, creator_id, table_name) VALUES (3, 1, 'initial-hard');

INSERT INTO words (word, definition, table_id) VALUES ('hello', 'Used as a greeting or to begin a phone conversation.', 1);