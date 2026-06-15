package com.punish.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jdbi.v3.core.Jdbi;

public class Database {
    private static Jdbi jdbi;

    public static Jdbi getJdbi(){
        if (jdbi == null) {
            String url      = System.getenv("DB_URL");
            String user     = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null) {
                try {
                    Properties props = new Properties();
                    InputStream input = Database.class.getClassLoader().getResourceAsStream("db.properties");
                    props.load(input);
                    url = props.getProperty("db.url");
                    user = props.getProperty("db.user");
                    password = props.getProperty("db.password");
                } catch (IOException e) {
                    System.out.println("Erro ao conectar com o banco: " + e.getMessage());
                }
            }
            jdbi = Jdbi.create(url, user, password);
        }
        return jdbi;
    }
}
