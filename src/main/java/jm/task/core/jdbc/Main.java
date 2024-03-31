package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = UserServiceImpl.getInstance();
        userService.createUsersTable();

        List<User> userList = new ArrayList<>();
        userList.add(new User("Ivan", "Ivanov", (byte) 35));
        userList.add(new User("Dmitrii", "Sergeev", (byte) 15));
        userList.add(new User("David", "Smolov", (byte) 24));
        userList.add(new User("Mark", "Smirnov", (byte) 47));


        for (User user : userList) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
            System.out.println("User с именем — " + user.getName() + " добавлен в базу данных");
        }

        System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        userService.dropUsersTable();

        UserDaoJDBCImpl.getInstance().closeConnection();
    }
}

