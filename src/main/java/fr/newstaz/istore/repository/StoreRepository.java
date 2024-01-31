package fr.newstaz.istore.repository;

import fr.newstaz.istore.model.Store;

import java.util.List;

public interface StoreRepository {

    void createStore(Store store);

    Store getStore(String name);

    void deleteStore(Store store);
    List<Store> getAllStores();
}
