package fr.newstaz.istore.repository;

import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;

import java.util.List;

public interface StoreRepository {

    void createStore(Store store);

    Store getStore(String name);

    void deleteStore(Store store);
    List<Store> getAllStores();

    void addEmployee(Store store, User user);

    boolean isEmployeeAlreadyAdded(User user, Store store);

    List<User> getEmployees(Store store);
}
