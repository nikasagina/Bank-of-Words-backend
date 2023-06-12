package com.example.bankofwords.config;

import com.example.bankofwords.dao.UserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/bank_of_words_db");
        dataSource.setUsername("user");
        dataSource.setPassword("rootroot");
        return dataSource;
    }

    @Bean
    public UserDAO userDAO(DataSource dataSource) {
        return new UserDAO(dataSource);
    }

}
