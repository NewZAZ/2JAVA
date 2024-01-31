package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.StoreResponse;

import java.util.List;
import java.util.function.Predicate;

public class StoreController {
    private final Repository repository;

    public StoreController(Repository repository) {
        this.repository = repository;
    }

    public StoreResponse.CreateStoreResponse createStore(String name) {
        Store store = new Store(name);
        if (repository.getStoreRepository().getStore(name) != null) {
            return new StoreResponse.CreateStoreResponse(false, "Store already exists");
        }

        repository.getStoreRepository().createStore(store);
        return new StoreResponse.CreateStoreResponse(true, "Store created");
    }

    public boolean deleteStore(Store store) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return false;
        }
        repository.getStoreRepository().deleteStore(store);
        return true;
    }

    public Store getStore(String name) {
        return repository.getStoreRepository().getStore(name);
    }

    public List<Store> getAllStore(Predicate<Store> predicate) {
        return repository.getStoreRepository().getAllStores().stream().filter(predicate).toList();
    }

    public List<Store> getAllStores() {
        return repository.getStoreRepository().getAllStores();
    }

    public List<Store> searchStores(String text) {
        return getAllStore(user -> user.getName().toLowerCase().contains(text.toLowerCase()));
    }

    public StoreResponse.AddEmployeeResponse addEmployee(Store store, String username) {
        if (repository.getUserRepository().getUser(username) == null) {
            return new StoreResponse.AddEmployeeResponse(false, "User not found");
        }

        if (repository.getStoreRepository().isEmployeeAlreadyAdded(repository.getUserRepository().getUser(username), store)) {
            return new StoreResponse.AddEmployeeResponse(false, "User already added");
        }

        repository.getStoreRepository().addEmployee(store, repository.getUserRepository().getUser(username));
        return new StoreResponse.AddEmployeeResponse(true, "User added");
    }
}
