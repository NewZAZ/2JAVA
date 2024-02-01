package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.Store;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.response.StoreResponse;
import fr.newstaz.istore.ui.component.ToastComponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UsersInStore extends JPanel {

    private final JFrame mainFrame;
    private final Controller controller;
    private final Store store;
    private JPanel usersPanel;

    public UsersInStore(JFrame mainFrame, Controller controller, Store store) {
        this.mainFrame = mainFrame;
        this.controller = controller;
        this.store = store;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        displayUsers(controller.getStoreController().getEmployees(store));

        // Wrap the usersPanel with a JScrollPane
        JScrollPane scrollPane = new JScrollPane(usersPanel);
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

        add(scrollPane, BorderLayout.CENTER); // Add the scrollPane instead of usersPanel directly
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Method to display users with a "DELETE" button
    private void displayUsers(List<User> userList) {
        usersPanel.removeAll();

        for (User user : userList) {
            JPanel userRow = new JPanel(new BorderLayout());

            JPanel userDetailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            userDetailsPanel.add(new JLabel("Email: " + user.getEmail()));

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton deleteButton = new JButton("DELETE");
            deleteButton.addActionListener(e -> {
                StoreResponse.RemoveEmployeeResponse removeEmployeeResponse = controller.getStoreController().removeEmployee(store, user);
                if (removeEmployeeResponse.success()) {
                    ToastComponent.showSuccessToast(this, removeEmployeeResponse.message());
                    SwingUtilities.invokeLater(() -> {
                        mainFrame.setContentPane(new UsersInStore(mainFrame, controller, store));
                        mainFrame.revalidate();
                    });
                } else {
                    ToastComponent.showFailedToast(this, removeEmployeeResponse.message());
                }
            });

            buttonPanel.add(deleteButton);

            userRow.add(userDetailsPanel, BorderLayout.CENTER);
            userRow.add(buttonPanel, BorderLayout.EAST);

            usersPanel.add(userRow);
        }

        usersPanel.revalidate();
        usersPanel.repaint();
    }
}