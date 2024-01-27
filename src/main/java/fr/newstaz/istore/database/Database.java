package fr.newstaz.istore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Database {

    private final Connection connection;

    private final Executor executor = new ScheduledThreadPoolExecutor(2);

    public Database(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
