package com.example.bankofwords.unittests;


import com.example.bankofwords.ConstantsForTests;
import com.example.bankofwords.dao.StatisticsDAO;
import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;

public class TestStatisticsDAO extends TestCase {

    private StatisticsDAO dao;

    protected void setUp() throws Exception {
        try {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDriverClassName(ConstantsForTests.TESTING_DB_CLASS_NAME);
            dataSource.setUrl(ConstantsForTests.TESTING_DB_URL);
            dataSource.setUsername(ConstantsForTests.TESTING_DB_USERNAME);
            dataSource.setPassword(ConstantsForTests.TESTING_DB_PASSWORD);
            dao = new StatisticsDAO(dataSource);
        } catch (Exception e) {
            throw new RuntimeException("Database connection error");
        }
    }


}