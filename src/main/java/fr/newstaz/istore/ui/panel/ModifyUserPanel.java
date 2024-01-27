package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.model.User;

import javax.swing.*;
import java.awt.*;

public class ModifyUserPanel extends Container {

    private final Controller controller;
    private final JFrame mainFrame;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;


    public ModifyUserPanel(Controller controller, JFrame mainFrame, User user) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init(user);
    }

    public void init(User user) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel emailLabel = new JLabel("Email:");
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        emailField = new JTextField(20);
        emailField.setText(user.getEmail());
        add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        add(confirmPasswordLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        confirmPasswordField = new JPasswordField(20);
        add(confirmPasswordField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Centrer horizontalement
        gbc.gridwidth = 2;
        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> performEdit(user));
        });
        add(editButton, gbc);
    }

    private void performEdit(User user) {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        /*if (!controller.getUserController().editUser(user, email, password)) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la modification de l'utilisateur", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }*/

        JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
        SwingUtilities.invokeLater(() -> {
            mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
            mainFrame.revalidate();
        });
    }
}
