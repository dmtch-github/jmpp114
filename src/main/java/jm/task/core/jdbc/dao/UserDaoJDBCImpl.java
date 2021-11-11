package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private final Logger log = Logger.getLogger(Class.class.getName());
    private final Util util;

    public UserDaoJDBCImpl() {
        util = new Util();
    }

    public void createUsersTable() {
        Connection connection = util.getConnection();
        if (connection != null) {
            try (Statement st = connection.createStatement()) {
                try {
                    st.execute("SELECT * FROM users");
                    log.info("Таблица найдена");
                } catch (SQLException e) {
                    String sqlCommand;
                    if (Util.flagMysql) { //строка MySQL
                        sqlCommand = "CREATE TABLE users (id INT NOT NULL AUTO_INCREMENT,name VARCHAR(45) NOT NULL,lastName VARCHAR(45) NOT NULL,age INT(3) NOT NULL,PRIMARY KEY (id), UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE);";
                    } else { //строка PostgreSQL
                        sqlCommand = "CREATE TABLE users (id SERIAL PRIMARY KEY, name CHARACTER VARYING(30) NOT NULL, lastName CHARACTER VARYING(30) NOT NULL, Age INTEGER NOT NULL);";
                    }
                    st.executeUpdate(sqlCommand);
                    log.info("Таблица создана");
                }
            } catch (SQLException e) {
                log.warning("Ошибка при создании Statement");
            }
            util.closeConnection();
        }
    }

    public void dropUsersTable() {
        Connection connection = util.getConnection();
        if (connection != null) {
            try (Statement st = connection.createStatement()) {
                    st.executeUpdate("DROP TABLE users");
                    log.info("Таблица удалена");
            } catch (SQLException e) {
                log.info("Таблица не найдена, удаление не требуется");
            }
            util.closeConnection();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = util.getConnection();
        if (connection != null) {
            try (PreparedStatement st = connection.prepareStatement("INSERT INTO users (name, lastName, age) VALUES (?,?,?);")) {
                st.setString(1, name);
                st.setString(2, lastName);
                st.setInt(3, age);
                st.executeUpdate();
                log.info("Запись добавлена в БД");
            } catch (SQLException e) {
                log.warning("Ошибка. Запись не добавлена в БД");
            }
            util.closeConnection();
        }
    }

    public void removeUserById(long id) {
        Connection connection = util.getConnection();
        if (connection != null) {
            try (PreparedStatement st = connection.prepareStatement("DELETE FROM users WHERE id=?;")) {
                st.setLong(1, id);
                int removeRow = st.executeUpdate();
                String mes = "Запись с id=" + id + (removeRow == 1?" удалена из БД":" не найдена в БД");
                log.info(mes);
            } catch (SQLException e) {
                log.warning("Ошибка. Запись не удалена из БД");
            }
            util.closeConnection();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Connection connection = util.getConnection();
        if (connection != null) {
            try (Statement st = connection.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT * FROM users");
                log.info("Данные получены из БД");
                while(rs.next()) {
                    list.add(new User(rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("lastName"),
                            (byte) rs.getInt("age")));
                }
            } catch (SQLException e) {
                log.warning("Ошибка. Данные не получены из БД");
            }
            util.closeConnection();
        }
        return list;
    }

    public void cleanUsersTable() {
        Connection connection = util.getConnection();
        if (connection != null) {
            try (Statement st = connection.createStatement()) {
                st.executeUpdate("DELETE FROM users");
                log.info("Таблица очищена");
            } catch (SQLException e) {
                log.warning("Таблица не найдена");
            }
            util.closeConnection();
        }
    }
}
