package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.Inventory;
import fr.newstaz.istore.model.InventoryItem;
import fr.newstaz.istore.model.Store;

import javax.swing.*;
import java.awt.*;

public class InventoryManagement extends JPanel {
    private final JFrame mainFrame;
    private final Controller controller;
    private final Store store;
    private JPanel inventoryPanel;

    public InventoryManagement(JFrame mainFrame, Controller controller, Store store) {
        this.mainFrame = mainFrame;
        this.controller = controller;
        this.store = store;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));
        displayInventory(store.getInventory());

        // Wrap the inventoryPanel with a JScrollPane
        JScrollPane scrollPane = new JScrollPane(inventoryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Back button
        JButton backButton = new JButton("BACK");
        backButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new StoreManagement(controller, mainFrame));
            mainFrame.revalidate();
        }));
        bottomPanel.add(backButton);

        JButton addButton = new JButton("ADD");
        addButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new AddItemToInventoryPanel(controller, mainFrame, store));
            mainFrame.revalidate();
        }));
        bottomPanel.add(addButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void displayInventory(Inventory inventory) {
        inventoryPanel.removeAll();

        for (InventoryItem item : inventory.getItems()) {
            JPanel itemRow = new JPanel(new BorderLayout());

            JPanel itemDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemDetailsPanel.add(new JLabel("Item: " + item.getName()));
            itemDetailsPanel.add(new JLabel("Price: " + item.getPrice()));

            JTextField quantityTextField = new JTextField(Integer.toString(item.getQuantity()));
            quantityTextField.setPreferredSize(new Dimension(50, 20));
            quantityTextField.addActionListener(e -> {
                controller.getStoreController().updateInventoryItem(store, item, Integer.parseInt(quantityTextField.getText()));
                item.setQuantity(Integer.parseInt(quantityTextField.getText()));
                displayInventory(inventory);
            });

            itemDetailsPanel.add(new JLabel("Quantity: "));
            itemDetailsPanel.add(quantityTextField);


            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton deleteButton = new JButton("DELETE");
            deleteButton.addActionListener(e -> {
                controller.getStoreController().removeInventoryItem(store, item);
                displayInventory(inventory);
            });

            buttonPanel.add(deleteButton);

            itemRow.add(itemDetailsPanel, BorderLayout.CENTER);
            itemRow.add(buttonPanel, BorderLayout.EAST);

            inventoryPanel.add(itemRow);
        }

        inventoryPanel.revalidate();
        inventoryPanel.repaint();
    }
}
