package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {
    private final Logger log = Logger.getLogger(Class.class.getName());
    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
         sessionFactory = Util.getSessionFactory();
        System.out.println("sessionFactory = " + sessionFactory);
    }


    @Override
    public void createUsersTable() {

    }

    @Override
    public void dropUsersTable() {

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

    }

    @Override
    public void removeUserById(long id) {

    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("\nЧтение записей таблицы");
        @SuppressWarnings("unchecked")
        List<User> list = (List<User>)sessionFactory.openSession().createQuery("from User").list();
        System.out.println("Вот что считали = " + list);
        return list;
    }

    @Override
    public void cleanUsersTable() {

    }
}
