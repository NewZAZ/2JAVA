package fr.newstaz.istore.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    public static boolean isValid(String email) {
        Pattern pattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }
}
