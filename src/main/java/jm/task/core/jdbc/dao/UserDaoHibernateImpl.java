package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private final Logger log = Logger.getLogger(UserDaoHibernateImpl.class.getName());
    private final SessionFactory factory;

    public UserDaoHibernateImpl() {
        factory = Util.getSessionFactoryHibernate();
    }

    @Override
    public void createUsersTable() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.createSQLQuery(Util.SQL_CMD_CREATE_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception ignore) {
            log.warning("Ошибка. При создании таблицы обнаружено " + ignore);
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = factory.getCurrentSession();) {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users;").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception ignore) {
            log.warning("Ошибка. При удалении таблицы обнаружено " + ignore);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (Exception ignore) {
            log.warning("Ошибка. При сохранении пользователя обнаружено " + ignore);
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = factory.getCurrentSession();) {
            session.beginTransaction();
            session.delete(session.get(User.class,id)); //session.createQuery("DELETE User WHERE id="+id).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception ignore) {
            log.warning("Ошибка. При удалении пользователя обнаружено " + ignore);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = factory.getCurrentSession();) {
            session.beginTransaction();
            List<User> users = session.createQuery("FROM User").getResultList();
            session.getTransaction().commit();
            return users;
        } catch (Exception ignore) {
            log.warning("Ошибка. При получении всех пользователей обнаружено " + ignore);
        }
        return new ArrayList<>();
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("DELETE User").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception ignore) {
            log.warning("Ошибка. При очистке таблицы обнаружено " + ignore);
        }
    }
}
