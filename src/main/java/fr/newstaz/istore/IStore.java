package fr.newstaz.istore;

import fr.newstaz.istore.controller.Controller;
import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.ui.MainFrame;

import javax.swing.*;
import java.sql.SQLException;

public class IStore {

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }

        System.out.println("MySQL JDBC Driver Registered!");

        AppConfig appConfig = new AppConfig();

        String url = appConfig.getDatabaseUrl();
        String username = appConfig.getDatabaseUsername();
        String password = appConfig.getDatabasePassword();

        System.out.println(url);
        System.out.println(password);
        System.out.println(username);
        Database database = null;
        try {
            database = new Database(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Repository repository = new Repository(database);

        Controller controller = new Controller(repository);


        SwingUtilities.invokeLater(() -> {
            new MainFrame(controller);
        });
    }
}
