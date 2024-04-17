package org.example.Object;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String DB_HOST;
    private final String DB_NAME;
    private final int DB_PORT;
    private final String DB_USER;
    private final String DB_PASSWORD;

    public Database() {
        Dotenv dotenv = Dotenv.configure()
                .load();

        DB_HOST = dotenv.get("DB_HOST");
        DB_NAME = dotenv.get("DB_NAME");
        DB_PORT = Integer.parseInt (dotenv.get("DB_PORT"));
        DB_USER = dotenv.get("DB_USER");
        DB_PASSWORD = dotenv.get("DB_PASSWORD");
    }

    public String getDB_NAME() {
        return DB_NAME;
    }

    public String getDB_USER() {
        return DB_USER;
    }

    public String getDB_PASSWORD() {
        return DB_PASSWORD;
    }

    public int getDB_PORT() {
        return DB_PORT;
    }

    public String getDB_HOST() {
        return DB_HOST;
    }

    public Connection getConnection (String host, String name, String user, String password) throws SQLException {

        return DriverManager.getConnection(
                "jdbc:mysql://" + host + "/" + name + "?autoReconnect=true",
                user,
                password
        );
    }
}
