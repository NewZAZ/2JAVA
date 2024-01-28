package fr.newstaz.istore.controller;

import fr.newstaz.istore.repository.Repository;

public class Controller {

    private final AuthenticationController authenticationController;

    private final UserController userController;

    public Controller(Repository repository) {
        this.userController = new UserController(repository);
        this.authenticationController = new AuthenticationController(repository, userController);
    }

    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    public UserController getUserController() {
        return userController;
    }
}
