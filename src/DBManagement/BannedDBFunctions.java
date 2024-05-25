package DBManagement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Retrieves string arrays from userDB
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/26/24
 */
public class BannedDBFunctions {
    private static final String LONGPATH = "/Users/schmidt/IdeaProjects/ITP265_Spring_2024/Final Project/BannedEmails";

    /**
     * Retrieves a list of banned emails from the database.
     *
     * @return an ArrayList of Strings containing the banned emails
     */
    public static ArrayList<String> getBannedEmails() {
        ArrayList<String> bannedEmails = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new FileInputStream(LONGPATH));
            while (sc.hasNextLine()) {
                bannedEmails.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) { System.out.println("BannedEmail.txt not setup correctly; change LONGPATH in BannedDBFunctions.java"); }
        return bannedEmails;
    }

    /**
     * Writes the list of banned emails to a file.
     *
     * @param bannedEmails the list of banned emails to be written to the file
     */
    public static void writeBannedEmailsToFile(ArrayList<String> bannedEmails) {
        try {
            PrintStream file = new PrintStream(LONGPATH);
            for (String email : bannedEmails) {
                file.println(email);
            }
        } catch (FileNotFoundException e) { System.out.println("BannedEmail.txt not setup correctly; change LONGPATH in BannedDBFunctions.java"); }
    }
}
