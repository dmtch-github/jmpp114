package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;


public class Main {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        //создать таблицу User
        userService.createUsersTable();

        //Добавить 4 юзера в таблицу , каждого вывести в консоль
        for(int i = 0; i < 4; i++) {
            String name = "User " + (i + 1);
            String lastName = "PreProject " + (i + 1) * 2;
            userService.saveUser(name,
                    lastName,
                    (byte) ((i + 1) * 3));
            System.out.println("User с именем - " + name + " добавлен в базу данных");
        }

        //userService.removeUserById(3);

        //получить всех юзеров из базы и вывести в консоль
        for (User user: userService.getAllUsers()) {
            System.out.println(user);
        }

        // очистить таблицу
        userService.cleanUsersTable();

        //удалить таблицу
        userService.dropUsersTable();
    }
}
