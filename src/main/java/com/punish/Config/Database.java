package com.punish.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jetty.http.HttpTester.Input;
import org.jdbi.v3.core.Jdbi;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {
    private static Jdbi jdbi;
    private static HikariDataSource datasSource;
    public static Properties loadProperties() throws IOException{
        Properties props = new Properties();
        try (InputStream input = Database.class.getResourceAsStream("/application.properties")) {
            if (input == null) {
                throw new IOException("Sorry, unable to find " + "/application.properties");
            }
            props.load(input);
        }
        return props;
    }

    public static HikariDataSource getDataSource(){
        if (datasSource == null) {
            try {
                Properties props = Database.loadProperties();
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(props.getProperty("db.url"));
                config.setUsername(props.getProperty("db.user"));
                config.setPassword(props.getProperty("db.password"));

                datasSource = new HikariDataSource(config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datasSource;
    }
}
