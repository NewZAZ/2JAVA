package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.UserResponse;
import fr.newstaz.istore.validator.UserValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.function.Predicate;

public class UserController {

    private final Repository repository;

    public UserController(Repository repository) {
        this.repository = repository;
    }

    public UserResponse.CreateUserResponse createUser(User user) {
        UserResponse userResponse = validateUser(user);
        if (!userResponse.success()) {
            return new UserResponse.CreateUserResponse(false, userResponse.message());
        }

        if (repository.getUserRepository().getUser(user.getEmail()) != null) {
            return new UserResponse.CreateUserResponse(false, "User already exists");
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        repository.getUserRepository().createUser(user);
        return new UserResponse.CreateUserResponse(true, "User created");
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

    public UserResponse.EditUserResponse editUser(User user, String email, String password, User.Role role) {
        User newUser = new User(user);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);
        UserResponse userResponse = validateUser(newUser);

        if (!userResponse.success()) {
            return new UserResponse.EditUserResponse(false, userResponse.message());
        }

        if (repository.getUserRepository().getUser(email) != null && !email.equals(user.getEmail())) {
            return new UserResponse.EditUserResponse(false, "User already exists");
        }

        newUser.setPassword(BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt()));
        repository.getUserRepository().updateUser(newUser);
        return new UserResponse.EditUserResponse(true, "User edited");
    }

    public boolean deleteUser(User user) {
        repository.getUserRepository().deleteUser(user);
        return true;
    }

    private UserResponse validateUser(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (email == null || email.isEmpty()) {
            return new UserResponse(false, "Email is empty");
        }

        if (password == null || password.isEmpty()) {
            return new UserResponse(false, "Password is empty");
        }

        if (!UserValidator.isValidEmail(email)) {
            return new UserResponse(false, "Email is not valid");
        }

        if (!UserValidator.isValidPassword(password)) {
            return new UserResponse(false, "Password is too short");
        }

        return new UserResponse(true, "User validated");
    }

    public void verifyUser(User user) {
        user.setVerified(true);
        repository.getUserRepository().updateUser(user);
    }

    public User getUser(String email) {
        return repository.getUserRepository().getUser(email);
    }

    public User getUserById(int id) {
        return repository.getUserRepository().getUserById(id);
    }
}
