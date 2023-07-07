package com.example.bankofwords.config;

import com.example.bankofwords.dao.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class Config {
    private static final String LEXICON_DB_URL = "jdbc:mysql://localhost:3306/wordnet_db";

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource bankDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public UserDAO userDAO(DataSource dataSource) {
        return new UserDAO(dataSource);
    }

    @Bean
    public WordDAO wordDAO(DataSource dataSource) {
        return new WordDAO(dataSource);
    }

    @Bean
    public StatisticsDAO statisticsDAO(DataSource dataSource) {
        return new StatisticsDAO(dataSource);
    }

    public DataSource lexiconDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(LEXICON_DB_URL);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public LexiconDAO lexiconDAO() {
        return new LexiconDAO(lexiconDataSource());
    }

    @Bean
    public Set<String> invalidatedTokens() {
        return new HashSet<>();
    }

    @Bean
    public ImageDAO imageDAO(DataSource dataSource) {
        return new ImageDAO(dataSource);
    }

    @Bean
    public WordHistoryDAO wordHistoryDAO(DataSource dataSource) {
        return new WordHistoryDAO(dataSource, new UserDAO(dataSource));
    }

    @Bean TableDAO tableDAO(DataSource dataSource) {
        return new TableDAO(dataSource);
    }
}
