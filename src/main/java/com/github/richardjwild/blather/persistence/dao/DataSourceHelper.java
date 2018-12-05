package com.github.richardjwild.blather.persistence.dao;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class DataSourceHelper {
    private static DataSource dataSource;

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost:5432/blather");
            dataSource.setUsername("codurance");
            dataSource.setPassword("1234");

            DataSourceHelper.dataSource = dataSource;
        }
        return dataSource;
    }
}
