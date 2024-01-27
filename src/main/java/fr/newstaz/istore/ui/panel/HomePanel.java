package fr.newstaz.istore.ui.panel;

import fr.newstaz.istore.controller.Controller;

import javax.swing.*;

public class HomePanel extends JPanel {

    private final Controller controller;
    private final JFrame mainFrame;

    public HomePanel(Controller controller, JFrame mainFrame) {
        this.controller = controller;
        this.mainFrame = mainFrame;
        init();
    }

    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton userManagementButton = new JButton("User management");

        userManagementButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                mainFrame.setContentPane(new UserManagementPanel(controller, mainFrame));
                mainFrame.revalidate();
            });
        });

        add(userManagementButton);
    }
}
