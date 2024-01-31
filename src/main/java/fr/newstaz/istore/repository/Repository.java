package fr.newstaz.istore.repository;

import fr.newstaz.istore.cache.StoreCache;
import fr.newstaz.istore.cache.UserCache;
import fr.newstaz.istore.database.Database;

public class Repository {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    public Repository(Database database) {
        this.userRepository = new UserCache(database);
        this.storeRepository = new StoreCache(database);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public StoreRepository getStoreRepository() {
        return storeRepository;
    }
}
