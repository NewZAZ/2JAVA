package fr.newstaz.istore.repository;

import fr.newstaz.istore.cache.StoreCache;
import fr.newstaz.istore.cache.UserCache;
import fr.newstaz.istore.dao.InventoryDAO;
import fr.newstaz.istore.database.Database;

public class Repository {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    private final InventoryRepository inventoryRepository;

    public Repository(Database database) {
        this.userRepository = new UserCache(database);
        this.inventoryRepository = new InventoryDAO(database);
        this.storeRepository = new StoreCache(database, inventoryRepository);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }

    public InventoryRepository getInventoryRepository() {
        return inventoryRepository;
    }
}
