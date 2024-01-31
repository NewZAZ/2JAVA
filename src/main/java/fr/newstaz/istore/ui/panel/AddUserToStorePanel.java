package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.response.StoreResponse;
import fr.newstaz.istore.ui.component.ToastComponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddUserToStorePanel extends JPanel {

    private final Controller controller;

    private final JFrame mainFrame;

    private JComboBox storeBox;
    private JComboBox userBox;

    public AddUserToStorePanel(Controller controller, JFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init();
    }

    public void init() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel storeLabel = new JLabel("Store:");
        add(storeLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        storeBox = new JComboBox();
        List<Store> stores = controller.getStoreController().getAllStores();
        for (Store store : stores) {
            storeBox.addItem(store.getName());
        }
        add(storeBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel userLabel = new JLabel("User:");
        add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        userBox = new JComboBox();
        List<User> users = controller.getUserController().getAllUsers();
        for (User user : users) {
            userBox.addItem(user.getEmail());
        }
        add(userBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(backButton, gbc);
        backButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new StoreManagement(controller, mainFrame));
            mainFrame.revalidate();
        }));
        JButton addButton = new JButton("Add");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(addButton, gbc);
        addButton.addActionListener(e -> {
            String storeName = (String) storeBox.getSelectedItem();
            String userEmail = (String) userBox.getSelectedItem();
            if (storeName == null || userEmail == null) {
                ToastComponent.showFailedToast(this, "Store or user cannot be empty");
                return;
            }

            Store store = controller.getStoreController().getStore(storeName);
            User user = controller.getUserController().getUser(userEmail);

            StoreResponse.AddEmployeeResponse addEmployeeResponse = controller.getStoreController().addEmployee(store, user.getEmail());
            if (!addEmployeeResponse.success()) {
                ToastComponent.showFailedToast(this, addEmployeeResponse.message());
                return;
            }
            ToastComponent.showSuccessToast(this, addEmployeeResponse.message());
            SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new StoreManagement(controller, mainFrame));
                mainFrame.revalidate();
            });
        });
    }

}

