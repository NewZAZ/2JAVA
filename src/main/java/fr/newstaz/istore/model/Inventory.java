package fr.newstaz.istore.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final int id;

    private final int storeId;

    private final List<InventoryItem> items = new ArrayList<>();

    public Inventory(int id, int storeId) {
        this.id = id;
        this.storeId = storeId;
    }

    public int getId() {
        return id;
    }

    public int getStoreId() {
        return storeId;
    }

    public List<InventoryItem> getItems() {
        return items;
    }

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public void removeItem(InventoryItem item) {
        items.remove(item);
    }
}
