package LoginFlow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
// import javax.xml.bind.DatatypeConverter;

/**
 * Password Regex for name, password, and email
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */
public class Regex {

    // LOL
    /**
     * Validates the given password according to specific rules.
     *
     * @param password The password to be validated.
     * @return An error message if the password does not meet the requirements, otherwise an empty string.
     * @throws IllegalArgumentException If the password is null.
     */
    public static String passwordRegex(String password) {
        if (password == null) throw new IllegalArgumentException();
        String[] regex = {"(?=.*[@#$%^&-+=()])^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$",
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$",
                "(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$",
                "(?=\\S+$).{8,20}$"};
        for (int i = regex.length - 1; i >= 0; i--) {
            Pattern pattern = Pattern.compile(regex[i]);
            Matcher matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                if (i == 0) return "Password must contain a special character";
                else if (i == 1) return "Password must contain a number";
                else if (i == 2) return "Password must contain a capital and lowercase letter";
                else return "Password must be 8 - 20 characters long";
            }
        }
        return "";
    }

    /**
     * Validates the given email according to a specific regex pattern.
     *
     * @param email The email to be validated.
     * @return An error message if the email is not valid, otherwise an empty string.
     * @throws IllegalArgumentException If the email is null.
     */
    public static String emailRegex(String email) {
        if (email == null) throw new IllegalArgumentException();
        String regex = "^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) return "You must input a valid email";
        return "";
    }

    /**
     * Validates the given name according to a specific regex pattern.
     *
     * @param name The name to be validated.
     * @return An error message if the name does not meet the requirements, otherwise an empty string.
     * @throws IllegalArgumentException If the name is null.
     */
    public static String nameRegex(String name) {
        if (name == null) throw new IllegalArgumentException();
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) return "Numbers and special characters not allowed";
        return "";
    }

    // testing
    public static void main(String[] args) {
        System.out.println(passwordRegex("hello"));
        System.out.println(passwordRegex("HHllo234"));
        System.out.println(passwordRegex("hellooooo"));
        System.out.println(passwordRegex("Helloooo2"));
        System.out.println(passwordRegex("Helloooo2#"));
        System.out.println(passwordRegex("Hellooo@o"));

        System.out.println(emailRegex("Helloo"));
        System.out.println(emailRegex("Hello@gmail.m"));
        System.out.println(emailRegex("Hellooo@gmail.com"));


    }
}
