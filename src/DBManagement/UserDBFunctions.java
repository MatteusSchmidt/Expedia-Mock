package DBManagement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

/**
 * Pulls data from the UserDB and transforms it for manipulation
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public class UserDBFunctions {
    private static final String LONGPATH = "/Users/schmidt/IdeaProjects/ITP265_Spring_2024/Final Project/UserDB";

    /**
     * Retrieves the user data by group from a file and returns a HashMap containing the data.
     *
     * @return A HashMap<String, ArrayList<String[]>> containing the user data grouped by type.
     *         The keys in the HashMap are "admin", "freeUser", and "paidUser".
     *         The values are ArrayLists of String arrays, where each String array represents a user's data.
     *         The structure of the String array depends on the user type:
     *         - For "admin" user type, the array has 7 elements: [type, UID, email, password, name, dateCreated].
     *         - For "freeUser" user type, the array has 8 elements: [type, UID, email, password, name, dateCreated, debt, listOfItineraries].
     *         - For "paidUser" user type, the array has 9 elements: [type, UID, email, password, name, dateCreated, debt, listOfItineraries, listOfSales].
     *         If the file is not found or cannot be read, an empty HashMap is returned.
     */
    public static HashMap<String,ArrayList<String[]>> getUsersByGroup() {
        ArrayList<String[]> freeUserData = new ArrayList<>();
        ArrayList<String[]> paidUserData = new ArrayList<>();
        ArrayList<String[]> adminData = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(LONGPATH);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Scanner line = new Scanner(sc.nextLine());
                line.useDelimiter(", ");
                String type = line.next();

                // admin, UID0000000001, email, password, name, datecreated
                if (type.equals("admin")) {
                    String[] admin = new String[7];
                    admin[0] = type;
                    for (int i = 1; i < admin.length; i++) admin[i] = line.next();
                    adminData.add(admin);

                // freeUser, UID0000000001, email, password, name, datecreated, debt, Listof(Inineraries)
                } else if (type.equals("freeUser")) {
                    String[] freeUser = new String[8];
                    freeUser[0] = type;
                    for (int i = 1; i < freeUser.length; i++) {
                        String nextToken = line.hasNext() ? line.next() : null;
                        freeUser[i] = nextToken;
                    }
                    freeUserData.add(freeUser);

                // freeUser, UID0000000001, email, password, name, datecreated, debt, Listof(Inineraries), Listof(Sales)
                } else {//(type.equals("paidUser")) {
                    String[] paidUser = new String[9];
                    paidUser[0] = type;
                    for (int i = 1; i < paidUser.length; i++) {
                        String nextToken = line.hasNext() ? line.next() : null;
                        paidUser[i] = nextToken;
                    }
                    paidUserData.add(paidUser);
                }
            }
            sc.close();
        } catch (FileNotFoundException e) { System.out.println("UserDB.txt not set up properly; change LONGPATH in UserDBFunctions.java"); }
        HashMap<String,ArrayList<String[]>> userData = new HashMap<>();
        userData.put("admin", adminData); userData.put("freeUser", freeUserData); userData.put("paidUser", paidUserData);
        return userData;
    }

    /**
     * Retrieves all users from the given dictionary and returns them as an ArrayList.
     *
     * @param dict The dictionary containing user data grouped by type. Each key in the dictionary represents a user type,
     *             and the corresponding value is an ArrayList of String arrays representing the user data.
     * @return An ArrayList of String arrays, where each String array represents a user's data.
     */
    public static ArrayList<String[]> getAllUsers(HashMap<String,ArrayList<String[]>> dict) {
        ArrayList<String[]> temp = new ArrayList<>();
        for (ArrayList<String[]> list : dict.values()) temp.addAll(list);
        return temp;
    }

    /**
     * Retrieves the non-admin users from the given dictionary and returns them as an ArrayList.
     *
     * @param dict The dictionary containing user data grouped by type. Each key in the dictionary represents a user type,
     *             and the corresponding value is an ArrayList of String arrays representing the user data.
     * @return An ArrayList of String arrays, where each String array represents a non-admin user's data.
     */
    public static ArrayList<String[]> getNonAdminUsers(HashMap<String,ArrayList<String[]>> dict) {
        ArrayList<String[]> temp = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String[]>> entry : dict.entrySet()) {
            if (!entry.getKey().equals("admin")) temp.addAll(entry.getValue());
        }
        return temp;
    }

    /**
     * Sets the UID for a given user based on the previous UID.
     * If the previous UID is null, null is returned.
     * If the previous UID is in the format*/
    public static String setUID(String previous) {
        if (previous == null) return null;
        String numIDStr = previous.replace("UID", "");
        try {
            int numID = Integer.parseInt(numIDStr);
            numID++;
            numIDStr = Integer.toString(numID);
            StringBuilder zeros = new StringBuilder();
            for (int i = 0; i < 10 - numIDStr.length(); i++) zeros.append("0");
            return "UID" + zeros + numIDStr;

        } catch (NumberFormatException e) {
            System.out.println("Test: must enter a valid UID format");
        }
        return null;
    }

    /**
     * Writes a list of user records to a file.
     *
     * @param strings An ArrayList of String arrays representing user records.
     *
     * Each String array represents a user's data and has the following structure:
     * - For "admin" user type, the array has 7 elements: [type, UID, email, password, name, dateCreated].
     * - For "freeUser" user type, the array has 8 elements: [type, UID, email, password, name, dateCreated, debt, listOfItineraries].
     * - For "paidUser" user type, the array has 9 elements: [type, UID, email, password, name, dateCreated, debt, listOfItineraries, listOfSales].
     *
     * This method writes each user record to the file specified by the LONGPATH constant in the UserDBFunctions class.
     * Each record is formatted in CSV (Comma-Separated Values) format.
     * If the file is not found or cannot be written to, the method prints an error message.
     */
    public static void writeUsersToFile(ArrayList<String[]> strings) {
        try {
            PrintStream file = new PrintStream(LONGPATH);
            for (String[] string : strings) {
                String str = CSVFormat(string);
                file.println(str);
            }
        } catch (FileNotFoundException e) { System.out.println("BannedEmail.txt not setup correctly; change LONGPATH in BannedDBFunctions.java"); }
    }

    /**
     * Formats an array of strings in CSV (Comma-Separated Values) format.
     *
     * @param strings The array of strings to format.
     * @return A string representing the formatted CSV.
     * @throws IllegalArgumentException if the input strings array is null.
     */
    private static String CSVFormat(String[] strings) {
        if (strings == null) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder();
        sb.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            sb.append(", ");
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        var test = new HashMap<String,ArrayList<String[]>>();
        test = UserDBFunctions.getUsersByGroup();
        System.out.println(test.get("freeUser"));
        System.out.println(test.get("admin"));
        System.out.println(test.get("paidUser"));
        System.out.println(test.get("freeUser").get(0)[1] + " " + test.get("freeUser").get(0)[6]);
        System.out.println(test.get("admin").get(0)[1] + " " + test.get("admin").get(0)[5]);
        System.out.println(test.get("paidUser").get(0)[1] + " " + test.get("paidUser").get(0)[7]);
        System.out.println(test.get("freeUser").get(1)[1] + " " + test.get("freeUser").get(0)[6]);

        System.out.println(setUID("UID0000000100"));
        System.out.println(setUID("UID0000000231"));
        System.out.println(setUID("UID0000000000"));
        System.out.println(setUID("hello"));
    }
}
