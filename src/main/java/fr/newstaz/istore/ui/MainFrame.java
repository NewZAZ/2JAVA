package fr.newstaz.istore.ui;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.ui.panel.LoginPanel;

import javax.swing.*;

public class MainFrame extends JFrame {

    private final Controller controller;

    public MainFrame(Controller controller) {
        super("iStore");
        this.controller = controller;
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LoginPanel loginPanel = new LoginPanel(controller, this);

        add(loginPanel);
        /*HomePanel homePanel = new HomePanel(controller, this);

        add(homePanel);*/

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
