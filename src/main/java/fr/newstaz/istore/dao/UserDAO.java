package fr.newstaz.istore.dao;

import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements UserRepository {

    private final Database database;

    public UserDAO(Database database) {
        this.database = database;
        createUserTable();
    }

    @Override
    public void createUser(User user) {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (email, password, role, is_verified) VALUES (?, ?, ?, ?)")) {
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getRole().name());
                statement.setBoolean(4, user.isVerified());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public User getUser(String login) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            statement.setString(1, login);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role") == null ? "USER" : resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void updateUser(User user) {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE users SET email = ?, password = ?, role = ?, is_verified = ? WHERE id = ?")) {
                statement.setString(1, user.getEmail());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getRole().name());
                statement.setBoolean(4, user.isVerified());
                statement.setInt(5, user.getId());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void deleteUser(User user) {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
                statement.setInt(1, user.getId());

                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        Connection connection = database.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        User.Role.valueOf(resultSet.getString("role") == null ? "USER" : resultSet.getString("role")),
                        resultSet.getBoolean("is_verified")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private void createUserTable() {
        Connection connection = database.getConnection();

        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, email VARCHAR(255), password VARCHAR(255), role VARCHAR(255), is_verified BOOLEAN)")) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
