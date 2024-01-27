package fr.newstaz.istore;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final String CONFIG_FILE = "config.properties";

    private Properties properties;

    public AppConfig() {
        loadConfig();
    }

    private void loadConfig() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                return;
            }
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDatabaseUrl() {
        return properties.getProperty("database.url");
    }

    public String getDatabaseUsername() {
        return properties.getProperty("database.username");
    }

    public String getDatabasePassword() {
        return properties.getProperty("database.password");
    }
}
