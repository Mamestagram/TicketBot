package org.example.Object;

import io.github.cdimascio.dotenv.Dotenv;

public class Bot {

    private String MYSQL_HOST;
    private String MYSQL_USER;
    private String MYSQL_PASSWORD;
    private String OSU_APIKEY;

    public Bot() {
        Dotenv dotenv = Dotenv.configure()
                .load();
        this.MYSQL_HOST = dotenv.get("HOST");
        this.MYSQL_USER = dotenv.get("USER");
        this.MYSQL_PASSWORD = dotenv.get("PASSWORD");
        this.OSU_APIKEY = dotenv.get("API");
    }

    public String getMYSQL_HOST() {
        return MYSQL_HOST;
    }

    public String getMYSQL_PASSWORD() {
        return MYSQL_PASSWORD;
    }

    public String getMYSQL_USER() {
        return MYSQL_USER;
    }

    public String getOSU_APIKEY() {
        return OSU_APIKEY;
    }
}
