package fr.newstaz.istore.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.newstaz.istore.dao.StoreDAO;
import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.repository.StoreRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class StoreCache implements StoreRepository {

    private final Cache<String, List<Store>> stores;

    private final StoreDAO storeDAO;

    public StoreCache(Database database) {
        this.stores = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.storeDAO = new StoreDAO(database);
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

        return storeDAO.getStore(name);
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
}
