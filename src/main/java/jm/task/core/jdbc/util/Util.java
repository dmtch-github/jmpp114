package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Util {
    private final Logger log = Logger.getLogger(Class.class.getName());
    public final static boolean flagMysql = true;
    private final String DB_DRIVER_NAME;
    private final String DB_URL;
    private final String DB_USER_NAME;
    private final String DB_USER_PASSWORD;

    private Connection connection = null;

    public Util() {
        if(flagMysql) {
            log.info("Работаю с БД MySQL");
            DB_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
            DB_URL = "jdbc:mysql://localhost:3306/mydbtest";
            DB_USER_NAME = "root";
            DB_USER_PASSWORD = "rootroot";
        } else {
            log.info("Работаю с БД PostgreSQL");
            DB_DRIVER_NAME = "org.postgresql.Driver";
            DB_URL = "jdbc:postgresql://localhost:5432/mydbtest";
            DB_USER_NAME = "admin";
            DB_USER_PASSWORD = "12345678";
        }
    }

    public Connection getConnection() {
        if(connection == null) {
            try {
                Class.forName(DB_DRIVER_NAME);
                connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_USER_PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    //Вопрос: как здесь правильно обрабатывать исключения?
    //Передавать выше, если Да, то каким классом? RuntimeException?
    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }
}
