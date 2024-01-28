package fr.newstaz.istore.validator;

public class UserValidator {

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
