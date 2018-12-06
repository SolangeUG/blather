package com.github.richardjwild.blather.helper;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataSourceManager {

    private DataSource dataSource;
    private String propertiesFileName;

    public DataSourceManager(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }

    public synchronized DataSource getDataSource() {

        if (dataSource == null) {

            InputStream input = ClassLoader.getSystemResourceAsStream(propertiesFileName);
            Properties properties = new Properties();

            try {
                properties.load(input);

                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName("org.postgresql.Driver");
                dataSource.setUrl(properties.getProperty("database.url"));
                dataSource.setUsername(properties.getProperty("database.user"));
                dataSource.setPassword(properties.getProperty("database.password"));

                this.dataSource = dataSource;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataSource;
    }
}
