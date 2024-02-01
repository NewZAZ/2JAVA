package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.Store;

import javax.swing.*;

public class AddItemToInventoryPanel extends JPanel {
    private final JFrame mainFrame;
    private final Controller controller;
    private final Store store;
    public AddItemToInventoryPanel(Controller controller, JFrame mainFrame, Store store) {
        this.mainFrame = mainFrame;
        this.controller = controller;
        this.store = store;
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Add item to inventory"));
        add(new JLabel("Item name:"));
        JTextField itemNameField = new JTextField();
        add(itemNameField);
        add(new JLabel("Item price:"));
        JTextField itemPriceField = new JTextField();
        add(itemPriceField);
        add(new JLabel("Item quantity:"));
        JTextField itemQuantityField = new JTextField();
        add(itemQuantityField);
        JButton addButton = new JButton("ADD");
        addButton.addActionListener(e -> {
            String itemName = itemNameField.getText();
            String itemPrice = itemPriceField.getText();
            controller.getStoreController().createInventoryItem(store, itemName, Integer.parseInt(itemPrice), Integer.parseInt(itemQuantityField.getText()));
            SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new InventoryManagement(mainFrame, controller, store));
                mainFrame.revalidate();
            });
        });
        add(addButton);
    }
}
