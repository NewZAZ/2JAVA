package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;

import java.util.List;
import java.util.function.Predicate;

public class UserController {

    private final Repository repository;

    public UserController(Repository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.getUserRepository().getAllUsers();
    }

    public List<User> getAllUsers(Predicate<User> predicate) {
        return repository.getUserRepository().getAllUsers().stream().filter(predicate).toList();
    }

    public List<User> searchUsers(String text) {
        return getAllUsers(user -> user.getEmail().toLowerCase().contains(text.toLowerCase()));
    }

    public boolean editUser(User user, String email, String password, User.Role role) {
        if (email.isBlank()) {
            return false;
        }

        if (password.isBlank()) {
            return false;
        }

        if (role == null) {
            return false;
        }

        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);

        repository.getUserRepository().updateUser(user);
        return true;
    }
}
