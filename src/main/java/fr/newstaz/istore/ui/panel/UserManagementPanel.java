package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagementPanel extends JPanel {

    private final Controller controller;
    private final JFrame mainFrame;

    private JTextField searchTextField;
    private JButton searchButton;
    private JPanel userPanel;
    private JButton addButton;

    public UserManagementPanel(Controller controller, JFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        // Champ de recherche
        searchTextField = new JTextField();
        searchButton = new JButton("SEARCH");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayUsers(controller.getUserController().searchUsers(searchTextField.getText()));
            }
        });

        // Panneau pour les utilisateurs
        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));

        displayUsers(controller.getUserController().getAllUsers());
        // Bouton d'ajout
        addButton = new JButton("AJOUTER UN UTILISATEUR");

        // Ajout des composants au panneau
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchTextField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(userPanel), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(addButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Méthode pour afficher les utilisateurs avec des boutons "MODIFIER"
    public void displayUsers(List<User> userList) {
        userPanel.removeAll(); // Efface les composants existants

        for (User user : userList) {
            JButton modifyButton = new JButton("MODIFIER");
            modifyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        mainFrame.setContentPane(new ModifyUserPanel(controller, mainFrame, user));
                        mainFrame.revalidate();
                    });
                }
            });

            JPanel userRow = new JPanel(new GridLayout(1, 2));

            userRow.add(new JLabel("Email: " + user.getEmail()));
            userRow.add(new JLabel("Role: " + user.getRole()));
            userRow.add(modifyButton, BorderLayout.EAST);

            userPanel.add(userRow);
        }

        userPanel.revalidate();
        userPanel.repaint();
    }
}
