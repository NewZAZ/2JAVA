package fr.newstaz.istore.dao;

import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.repository.StoreRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO implements StoreRepository {
    private final Database database;

    public StoreDAO(Database database) {
        this.database = database;
    }

    @Override
    public void createStore(Store store) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO stores (name, address, phone, email, website, description) VALUES (?, ?, ?, ?, ?, ?)")) {
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
    public void updateStore(Store store) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE stores SET name = ? WHERE id = ?")) {
                statement.setString(1, store.getName());
                statement.setInt(2, store.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void deleteStore(Store store) {
        Connection connection = database.getConnection();
        database.execute(() -> {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM stores WHERE id = ?")) {
                statement.setInt(1, store.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
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
                stores.add(new Store(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stores;
    }

}