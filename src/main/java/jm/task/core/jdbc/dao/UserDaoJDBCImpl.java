package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.exception.DaoException;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        final String CREATE_SQL = """
                CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY ,
                name TEXT,
                lastname TEXT,
                age VARCHAR(128)
                );
                """;
        try (Connection connection = Util.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_SQL);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void dropUsersTable() {
        final String DROP_SQL = """
                DROP TABLE IF EXISTS users;
                """;
        try (Connection connection = Util.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_SQL);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        final String SAVE_SQL = """
                INSERT INTO users(name, lastname, age) 
                VALUES (?,?,?);
                """;
        try (Connection connection = Util.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void removeUserById(long id) {
        final String REMOVE_SQL = """
                DELETE FROM users
                WHERE id=?;
                """;
        try (Connection connection = Util.open();
             PreparedStatement statement = connection.prepareStatement(REMOVE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new DaoException(exception);
        }
    }

    public List<User> getAllUsers() {
        final String GET_ALL_SQL = """
                SELECT 
                id,
                name,
                lastname,
                age
                FROM users;
                """;
        List<User> userList = new ArrayList<>();
        try (Connection connection = Util.open();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet resultSet = statement.executeQuery();
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
        final String CLEAN_SQL = """
                DELETE FROM users;
                """;
        try (Connection connection = Util.open();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAN_SQL);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
