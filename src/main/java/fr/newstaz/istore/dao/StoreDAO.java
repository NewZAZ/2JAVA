package fr.newstaz.istore.dao;

import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.StoreRepository;
import fr.newstaz.istore.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO implements StoreRepository {
    private final Database database;

    private final UserRepository userRepository;

    public StoreDAO(Database database, UserRepository userRepository) {
        this.database = database;
        this.userRepository = userRepository;
        createTable();
    }

    @Override
    public void createStore(Store store) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO stores (name) VALUES (?)")) {
                statement.setString(1, store.getName());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Store getStore(String name) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores WHERE name = ?")) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Store(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void deleteStore(Store store) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM stores WHERE id = ?")) {
                System.out.println(store.getId());
                statement.setInt(1, store.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<Store> getAllStores() {
        Connection connection = database.getConnection();
        List<Store> stores = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Store store = new Store(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
                store.setEmployees(getEmployees(store));
                stores.add(store);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stores;
    }

    @Override
    public void addEmployee(Store store, User user) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO stores_employee (store_id, employee_id) VALUES (?, ?)")) {
                statement.setInt(1, store.getId());
                statement.setInt(2, user.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public boolean isEmployeeAlreadyAdded(User user, Store store) {
        Connection connection = database.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores_employee WHERE store_id = ? AND employee_id = ?")) {
            statement.setInt(1, store.getId());
            statement.setInt(2, user.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public List<User> getEmployees(Store store) {
        Connection connection = database.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM stores_employee WHERE store_id = ?")) {
            statement.setInt(1, store.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(userRepository.getUser(resultSet.getInt("employee_id")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }


    public void createTable() {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS stores (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) UNIQUE NOT NULL)"
            )) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS stores_employee (id INT PRIMARY KEY AUTO_INCREMENT, store_id INT NOT NULL, employee_id INT NOT NULL, FOREIGN KEY (store_id) REFERENCES stores(id), FOREIGN KEY (employee_id) REFERENCES employees(id))"
            )) {
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}