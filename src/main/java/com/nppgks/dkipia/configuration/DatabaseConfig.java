package com.nppgks.dkipia.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.nppgks.dkipia.util.Constant;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(Constant.DB.URL);
        dataSource.setUsername(Constant.DB.USERNAME);
        dataSource.setPassword(Constant.DB.PASSWORD);
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

}
