package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    private static final Logger log = Logger.getLogger(Util.class.getName());
    public static final boolean FLAG_USE_MYSQL = true;
    public static final String SQL_CMD_CREATE_TABLE;
    private static final String DB_DRIVER_NAME;
    private static final String DB_URL;
    private static final String DB_DIALECT;
    private static final String DB_USER_NAME;
    private static final String DB_USER_PASSWORD;
    private static final SessionFactory FACTORY;

    static {
        if(FLAG_USE_MYSQL) {
            DB_DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
            DB_URL = "jdbc:mysql://localhost:3306/mydbtest";
            DB_DIALECT = "org.hibernate.dialect.MySQL5Dialect";
            DB_USER_NAME = "root";
            DB_USER_PASSWORD = "rootroot";
            SQL_CMD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (id INT NOT NULL AUTO_INCREMENT,name VARCHAR(45) NOT NULL,lastName VARCHAR(45) NOT NULL,age INT(3) NOT NULL,PRIMARY KEY (id), UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE);";
        } else {
            DB_DRIVER_NAME = "org.postgresql.Driver";
            DB_URL = "jdbc:postgresql://localhost:5432/mydbtest";
            DB_DIALECT = "org.hibernate.dialect.PostgreSQL94Dialect";
            DB_USER_NAME = "admin";
            DB_USER_PASSWORD = "12345678";
            SQL_CMD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name CHARACTER VARYING(30) NOT NULL, lastName CHARACTER VARYING(30) NOT NULL, Age INTEGER NOT NULL);";
        }
        FACTORY = buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()//.configure("hibernate.cfg.xml")
                    .setProperty("hibernate.connection.url", DB_URL)
                    .setProperty("connection.driver_class", DB_DRIVER_NAME)
                    .setProperty("hibernate.connection.username", DB_USER_NAME)
                    .setProperty("hibernate.connection.password", DB_USER_PASSWORD)
                    .setProperty("dialect", DB_DIALECT)
                    .setProperty("hibernate.current_session_context_class", "thread")
                    .setProperty("hibernate.show_sql", "true")
                    //.setProperty("hibernate.hbm2ddl.auto", "update")
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            log.log(Level.WARNING,"Ошибка при создании объекта sessionFactory {0}", e.toString());
            throw e;
        }
    }

    private Util() {

    }

    public static SessionFactory getSessionFactoryHibernate() {
        return FACTORY;
    }

    public static void shutdown() {
        getSessionFactoryHibernate().close();
    }

    public static Connection getConnectionJDBC() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER_NAME);
            connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_USER_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnectionJDBC(Connection connection) {
        try {
            connection.close();
        } catch (SQLException | NullPointerException e) {
            log.warning("При работе closeConnectionJDBC возникла ошибка " + e);
        }
    }
}
