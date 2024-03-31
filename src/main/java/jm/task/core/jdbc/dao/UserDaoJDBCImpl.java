package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.exception.DaoException;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public static final UserDaoJDBCImpl INSTANCE = new UserDaoJDBCImpl();
    public static final Connection connection;

    static {
        try {
            connection = Util.open();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    private static final String CREATE_SQL = """
            CREATE TABLE IF NOT EXISTS users (
            id SERIAL PRIMARY KEY ,
            name TEXT,
            lastname TEXT,
            age VARCHAR(128)
            );
            """;
    private static final String DROP_SQL = """
            DROP TABLE IF EXISTS users;
            """;
    private static final String SAVE_SQL = """
            INSERT INTO users(name, lastname, age) 
            VALUES (?,?,?);
            """;
    private static final String REMOVE_SQL = """
            DELETE FROM users
            WHERE id=?;
            """;
    private static final String GET_ALL_SQL = """
            SELECT 
            id,
            name,
            lastname,
            age
            FROM users;
            """;
    private static final String CLEAN_SQL = """
            DELETE FROM users;
            """;

    public UserDaoJDBCImpl() {
    }

    public static UserDaoJDBCImpl getInstance() {
        return INSTANCE;
    }

    public void createUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_SQL);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void dropUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(DROP_SQL);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_SQL);
             ResultSet resultSet = statement.executeQuery();
        ) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));

                userList.add(user);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(CLEAN_SQL);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }
    }
}
