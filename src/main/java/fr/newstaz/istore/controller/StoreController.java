package fr.newstaz.istore.controller;

import fr.newstaz.istore.cache.StoreCache;
import fr.newstaz.istore.model.Inventory;
import fr.newstaz.istore.model.InventoryItem;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.StoreResponse;

import java.util.List;
import java.util.Objects;
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
        Store newStore = repository.getStoreRepository().getStore(name);
        repository.getInventoryRepository().createInventory(new Inventory(0, newStore.getId()));
        StoreCache storeCache = (StoreCache) repository.getStoreRepository();
        storeCache.invalidCache();
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

    public List<User> getEmployees(Store store) {
        return repository.getStoreRepository().getEmployees(store);
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

    public StoreResponse.RemoveEmployeeResponse removeEmployee(Store store, User user) {
        if (repository.getUserRepository().getUser(user.getEmail()) == null || repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.RemoveEmployeeResponse(false, "User or store not found");
        }
        repository.getStoreRepository().removeEmployee(store, repository.getUserRepository().getUserById(user.getId()));
        return new StoreResponse.RemoveEmployeeResponse(true, "User removed");
    }

    public StoreResponse.CreateInventoryItemResponse createInventoryItem(Store store, String name, int price, int quantity) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.CreateInventoryItemResponse(false, "Store not found");
        }
        if (repository.getInventoryRepository().getInventory(store.getId()).getItems().stream().filter(inventoryItem -> inventoryItem.getName() != null).anyMatch(inventoryItem -> inventoryItem.getName().equals(name))) {
            return new StoreResponse.CreateInventoryItemResponse(false, "Inventory item already exists");
        }
        repository.getInventoryRepository().addItemToInventory(store.getInventory(), new InventoryItem(0, name, price, quantity));
        return new StoreResponse.CreateInventoryItemResponse(true, "Inventory item created");
    }
    public StoreResponse.UpdateInventoryItemResponse updateInventoryItem(Store store, InventoryItem inventoryItem, int quantity) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.UpdateInventoryItemResponse(false, "Store not found");
        }
        if (repository.getInventoryRepository().getInventory(store.getId()).getItems().stream().filter(inventoryItem1 -> inventoryItem1.getName() != null).noneMatch(inventoryItem1 -> inventoryItem1.getName().equals(inventoryItem.getName()))) {
            return new StoreResponse.UpdateInventoryItemResponse(false, "Inventory item not found");
        }
        repository.getInventoryRepository().updateItemInInventory(store.getInventory(), new InventoryItem(inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getPrice(), quantity));
        return new StoreResponse.UpdateInventoryItemResponse(true, "Inventory item updated");
    }

    public StoreResponse.DeleteInventoryItemResponse removeInventoryItem(Store store, InventoryItem inventoryItem) {
        if (repository.getStoreRepository().getStore(store.getName()) == null) {
            return new StoreResponse.DeleteInventoryItemResponse(false, "Store not found");
        }
        if (repository.getInventoryRepository().getInventory(store.getId()).getItems().stream().filter(inventoryItem1 -> inventoryItem1.getName() != null).noneMatch(inventoryItem1 -> inventoryItem1.getName().equals(inventoryItem.getName()))) {
            return new StoreResponse.DeleteInventoryItemResponse(false, "Inventory item not found");
        }
        repository.getInventoryRepository().deleteItemFromInventory(store.getInventory(), new InventoryItem(inventoryItem.getId(), inventoryItem.getName(), inventoryItem.getPrice(), inventoryItem.getQuantity()));
        return new StoreResponse.DeleteInventoryItemResponse(true, "Inventory item deleted");
    }

}
