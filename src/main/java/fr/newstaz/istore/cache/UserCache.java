package fr.newstaz.istore.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.newstaz.istore.dao.UserDAO;
import fr.newstaz.istore.database.Database;
import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.UserRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserCache implements UserRepository {

    private final Cache<String, List<User>> users;

    private final UserDAO userDAO;

    public UserCache(Database database) {
        this.users = CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
        this.userDAO = new UserDAO(database);

    }

    @Override
    public void createUser(User user) {
        userDAO.createUser(user);

        users.invalidateAll();
    }

    @Override
    public User getUser(int id) {
        List<User> users = this.users.getIfPresent("users");

        if (users == null) {
            users = userDAO.getAllUsers();

            this.users.put("users", users);
        }

        return users.stream().filter(user -> user.getId() == id).findFirst().orElse(null);
    }

    @Override
    public User getUser(String login) {
        List<User> users = this.users.getIfPresent("users");

        if (users == null) {
            users = userDAO.getAllUsers();

            this.users.put("users", users);
        }

        return users.stream().filter(user -> user.getEmail().equals(login)).findFirst().orElse(null);
    }

    @Override
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);

        users.invalidateAll();
    }

    @Override
    public void deleteUser(User user) {
        userDAO.deleteUser(user);

        users.invalidateAll();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = this.users.getIfPresent("users");

        if (users == null) {
            users = userDAO.getAllUsers();

            this.users.put("users", users);
        }

        return users;
    }
}
