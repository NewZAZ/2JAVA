package fr.newstaz.istore.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.newstaz.istore.dao.StoreDAO;
import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.StoreRepository;
import fr.newstaz.istore.repository.UserRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StoreCache implements StoreRepository {

    private final Cache<String, List<Store>> stores;

    private final StoreDAO storeDAO;

    public StoreCache(Database database, UserRepository userRepository) {
        this.stores = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.storeDAO = new StoreDAO(database, userRepository);
    }

    @Override
    public void createStore(Store store) {
        storeDAO.createStore(store);

        stores.invalidateAll();
    }

    @Override
    public Store getStore(String name) {
        List<Store> stores = this.stores.getIfPresent("stores");

        if (stores == null) {
            stores = storeDAO.getAllStores();

            this.stores.put("stores", stores);
        }

        return stores.stream().filter(store -> store.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void deleteStore(Store store) {
        storeDAO.deleteStore(store);

        stores.invalidateAll();
    }

    @Override
    public List<Store> getAllStores() {
        List<Store> stores = this.stores.getIfPresent("stores");
        if (stores == null) {
            stores = storeDAO.getAllStores();
            this.stores.put("stores", stores);
        }
        return stores;
    }

    @Override
    public void addEmployee(Store store, User user) {
        storeDAO.addEmployee(store, user);
        stores.invalidateAll();
    }

    @Override
    public boolean isEmployeeAlreadyAdded(User user, Store store) {
        return storeDAO.isEmployeeAlreadyAdded(user, store);
    }

    @Override
    public List<User> getEmployees(Store store) {
        return storeDAO.getEmployees(store);
    }
}
