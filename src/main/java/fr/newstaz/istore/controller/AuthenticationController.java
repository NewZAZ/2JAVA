package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.LoginResponse;
import fr.newstaz.istore.response.RegisterResponse;
import fr.newstaz.istore.validator.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationController {

    private final Repository repository;

    private User loggedUser;

    public AuthenticationController(Repository repository) {
        this.repository = repository;
    }

    public RegisterResponse register(String email, String password) {
        if (repository.getUserRepository().getUser(email) != null) {
            return new RegisterResponse(false, "User already exists");
        }

        if (email == null || email.isEmpty()) {
            return new RegisterResponse(false, "Email is empty");
        }

        if (password == null || password.isEmpty()) {
            return new RegisterResponse(false, "Password is empty");
        }

        if (!EmailValidator.isValid(email)) {
            return new RegisterResponse(false, "Email is not valid");
        }

        if (password.length() < 8) {
            return new RegisterResponse(false, "Password is too short");
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(email, hashedPassword);

        repository.getUserRepository().createUser(user);
        return new RegisterResponse(true, "User created");
    }

    public LoginResponse login(String email, String password) {
        if (email == null || email.isEmpty()) {
            return new LoginResponse(false, "Email is empty");
        }

        if (!EmailValidator.isValid(email)) {
            return new LoginResponse(false, "Email is not valid");
        }

        if (password == null || password.isEmpty()) {
            return new LoginResponse(false, "Password is empty");
        }

        User user = repository.getUserRepository().getUser(email);

        if (user == null) {
            return new LoginResponse(false, "User not found");
        }

        String hashedPassword = user.getPassword();

        if (!BCrypt.checkpw(password, hashedPassword)) {
            return new LoginResponse(false, "Wrong password");
        }
        loggedUser = user;
        return new LoginResponse(true, "User logged in");
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}
