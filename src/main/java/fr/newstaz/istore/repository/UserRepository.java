package fr.newstaz.istore.repository;

import fr.newstaz.istore.model.User;

import java.util.List;

public interface UserRepository {

    void createUser(User user);
    User getUser(int id);
    User getUser(String login);
    void updateUser(User user);
    void deleteUser(User user);
    List<User> getAllUsers();
}
