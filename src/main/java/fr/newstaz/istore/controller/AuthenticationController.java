package fr.newstaz.istore.controller;

import fr.newstaz.istore.model.User;
import fr.newstaz.istore.repository.Repository;
import fr.newstaz.istore.response.LoginResponse;
import fr.newstaz.istore.response.RegisterResponse;
import fr.newstaz.istore.response.UserResponse;
import org.mindrot.jbcrypt.BCrypt;

public class AuthenticationController {

    private final Repository repository;
    private final UserController userController;

    private User loggedUser;

    public AuthenticationController(Repository repository, UserController userController) {
        this.repository = repository;
        this.userController = userController;
    }

    public RegisterResponse register(String email, String password) {
        UserResponse.CreateUserResponse response = userController.createUser(new User(email, BCrypt.hashpw(password, BCrypt.gensalt()), User.Role.USER));

        if (!response.success()) {
            return new RegisterResponse(false, response.message());
        }
        return new RegisterResponse(true, "User created");
    }

    public LoginResponse login(String email, String password) {
        if (email == null || email.isEmpty()) {
            return new LoginResponse(false, "Email is empty");
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

        if (!user.isVerified()) {
            return new LoginResponse(false, "User is not verified");
        }

        loggedUser = user;
        return new LoginResponse(true, "User logged in");
    }

    public User getLoggedUser() {
        return loggedUser;
    }
}