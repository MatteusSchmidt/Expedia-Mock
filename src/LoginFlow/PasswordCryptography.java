package LoginFlow;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes passwords to Cryptographic Strings for storage
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/24/24
 */
public class PasswordCryptography {

    /**
     * Applies cryptographic hashing to the given password.
     *
     * @param password The password to be hashed.
     * @return The hashed password as a string.
     */
    public static String passwordCryptography(String password) {
        // ur welcome
        String pepper = System.getenv("sPEPPER_VALUE");
        password = (pepper != null) ? pepper + password : password;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            // return DatatypeConverter.printHexBinary(digest).toUpperCase();
            // or
            return bytesToHex(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e){
            System.out.println("Unable to access MD5 crypto algo");
        }
        return password;
    }

    /**
     * Converts a byte array to a hexadecimal string representation.
     *
     * **Used StackOverflow to avoid using maven**
     *
     * @param bytes The byte array to be converted.
     * @return The hexadecimal string representation of the byte array.
     */
    // to avoid import of javax.xml.bind.DatatypeConverter which doesn't come standard
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString( b & 0xFF);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        System.out.println(passwordCryptography("hello"));
        System.out.println(passwordCryptography("hello"));
        System.out.println(passwordCryptography("Password@123"));
    }
}
