package com.example.bankofwords;

import com.example.bankofwords.dao.WordDAO;
import com.example.bankofwords.objects.Word;
import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;

public class WordDAOtest extends TestCase {

    private WordDAO dao;

    protected void setUp() throws Exception {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/bank_of_words_db");
            dataSource.setUsername("user");
            dataSource.setPassword("rootroot");
            dao =  new WordDAO(dataSource);
        } catch (Exception e){
            throw new RuntimeException("Database connection error");
        }
    }

    public void testGetRandomWord() {
        Word randomWord = dao.getRandomWord(1);
        System.out.println(randomWord.toString());
    }

    public void testGetIncorrectWords() {
        Word randomWord = dao.getRandomWord(1);
        System.out.println("Correct:");
        System.out.println(randomWord.toString());
        System.out.println("Incorrect:");
        System.out.println(dao.getIncorrectWords(randomWord, 1).toString());

    }
}
