CREATE DATABASE IF NOT EXISTS bank_of_words_db;
USE bank_of_words_db;


DROP TABLE IF EXISTS word_statistics;
DROP TABLE IF EXISTS known_words;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       join_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
INSERT INTO words (word, definition) VALUES ('artificial intelligence', 'The simulation of human intelligence in machines that are programmed to think and learn like humans.');
INSERT INTO words (word, definition) VALUES ('cloud computing', 'The delivery of computing services over the internet, including storage, servers, databases, and software.');
INSERT INTO words (word, definition) VALUES ('cyber security', 'The practice of protecting computer systems, networks, and data from digital attacks and unauthorized access.');
INSERT INTO words (word, definition) VALUES ('machine learning', 'A subset of artificial intelligence that enables systems to learn and improve from experience without being explicitly programmed.');
INSERT INTO words (word, definition) VALUES ('data mining', 'The process of discovering patterns, correlations, and insights from large datasets using various techniques.');
INSERT INTO words (word, definition) VALUES ('virtual reality', 'A simulated experience that can be similar to or completely different from the real world, typically achieved through computer-generated environments.');
INSERT INTO words (word, definition) VALUES ('cat', 'A small domesticated carnivorous mammal with soft fur, a short snout, and retractile claws.');
INSERT INTO words (word, definition) VALUES ('book', 'A written or printed work consisting of pages glued or sewn together along one side and bound in covers.');
INSERT INTO words (word, definition) VALUES ('tree', 'A woody perennial plant, typically having a single stem or trunk growing to a considerable height.');
INSERT INTO words (word, definition) VALUES ('car', 'A road vehicle, typically with four wheels, powered by an internal combustion engine or electric motor.');
INSERT INTO words (word, definition) VALUES ('house', 'A building for human habitation, typically one that is lived in by a family or small group of people.');
INSERT INTO words (word, definition) VALUES ('computer', 'An electronic device capable of storing, retrieving, and processing data, typically performing various tasks by executing pre-programmed instructions.');
INSERT INTO words (word, definition) VALUES ('phone', 'A telecommunications device that allows two or more users to conduct a conversation when they are too far apart to be heard directly.');
INSERT INTO words (word, definition) VALUES ('flower', 'The reproductive structure found in plants, typically colorful and fragrant, and bearing seeds or fruit.');
INSERT INTO words (word, definition) VALUES ('music', 'Vocal or instrumental sounds combined in such a way as to produce beauty of form, harmony, and expression of emotion.');
