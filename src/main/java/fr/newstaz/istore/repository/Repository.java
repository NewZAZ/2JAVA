package fr.newstaz.istore.repository;

import fr.newstaz.istore.cache.UserCache;
import fr.newstaz.istore.database.Database;

public class Repository {

    private final UserRepository userRepository;

    public Repository(Database database) {
        this.userRepository = new UserCache(database);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
