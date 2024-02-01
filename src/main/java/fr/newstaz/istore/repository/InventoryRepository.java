package fr.newstaz.istore.repository;

import fr.newstaz.istore.model.Inventory;
import fr.newstaz.istore.model.InventoryItem;

public interface InventoryRepository {
    void createInventory(Inventory inventory);

    Inventory getInventory(int id);

    void updateInventory(Inventory inventory);

    void deleteInventory(Inventory inventory);

    void addItemToInventory(Inventory inventory, InventoryItem item);

    void updateItemInInventory(Inventory inventory, InventoryItem item);

    void deleteItemFromInventory(Inventory inventory, InventoryItem item);

}
