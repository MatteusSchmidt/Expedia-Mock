package UserHierarchy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * POJO to represent an Admin
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */
public class Admin extends User {
    private ArrayList<String> bans;
    public Admin(String userType, String UID, String email, String password, String name, LocalDate created, ArrayList<String> bans) {
        super(userType, UID, email, password, name, created);
        this.bans = bans;
    }
    public ArrayList<String> getBans() { return bans; }
    public void setBans(ArrayList<String> bans) { this.bans = bans; }

    /**
     * Converts an ArrayList of bans into a string representation.
     * If the bans ArrayList is null or empty, returns the string "null".
     * Otherwise, concatenates the bans into a comma-separated string.
     *
     * @param bans the ArrayList of bans to convert into a string
     * @return the string representation of the bans
     */
    public static String bansToString(ArrayList<String> bans) {
        if (bans == null || bans.isEmpty()) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append(bans.get(0));
        if (bans.size() > 1) {
            for (int i = 1; i < bans.size(); i++) {
                sb.append(",");
                sb.append(bans.get(i));
            }
        }
        return sb.toString();
    }

    public static ArrayList<String> createBans(String bans) {
        ArrayList<String> bannedUsers = new ArrayList<>();
        Scanner sc = new Scanner(bans);
        sc.useDelimiter(",");
        while (sc.hasNext()) bannedUsers.add(sc.next());
        return bannedUsers;
    }
}
