package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {
    private final Logger log = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public void createUsersTable() {
        Connection connection = Util.getConnectionJDBC();
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(Util.SQL_CMD_CREATE_TABLE);
        } catch (SQLException e) {
            log.warning("Ошибка. Не создан Statement или таблица существует");
        }
        Util.closeConnectionJDBC(connection);
    }

    public void dropUsersTable() {
        Connection connection = Util.getConnectionJDBC();
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            log.warning("Ошибка при создании Statement");
        }
        Util.closeConnectionJDBC(connection);
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = Util.getConnectionJDBC();
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO users (name, lastName, age) VALUES (?,?,?);")) {
            st.setString(1, name);
            st.setString(2, lastName);
            st.setInt(3, age);
            st.executeUpdate();
        } catch (SQLException e) {
            log.warning("Ошибка. Не создан PreparedStatement или не найдена таблица");
        }
        Util.closeConnectionJDBC(connection);
    }

    public void removeUserById(long id) {
        Connection connection = Util.getConnectionJDBC();
        try (PreparedStatement st = connection.prepareStatement("DELETE FROM users WHERE id=?;")) {
            st.setLong(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            log.warning("Ошибка. Не создан PreparedStatement или не найдена таблица");
        }
        Util.closeConnectionJDBC(connection);
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        Connection connection = Util.getConnectionJDBC();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while(rs.next()) {
                list.add(new User(rs.getString("name"),
                        rs.getString("lastName"),
                        (byte) rs.getInt("age")));
            }
        } catch (SQLException e) {
            log.warning("Ошибка. Не создан Statement или не найдена таблица");
        }
        Util.closeConnectionJDBC(connection);
        return list;
    }

    public void cleanUsersTable() {
        Connection connection = Util.getConnectionJDBC();
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM users");
        } catch (SQLException e) {
            log.warning("Ошибка. Не создан Statement или не найдена таблица");
        }
        Util.closeConnectionJDBC(connection);
    }
}
