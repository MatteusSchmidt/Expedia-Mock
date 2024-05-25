/**
 * All types of menu options available
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public enum MenuOptions {
    PROFILE,
    BAN,
    PAYOUT,
    INDEBTED_USERS,
    PLAN_TRIP,
    SELL,
    LOGOUT,
    QUIT;

    /**
     * Prints all the options
     */
    public static void printMenuOptions(String userType, String name) {
        for (int i = 0; i < 35; i++) System.out.print("-");
        System.out.println("\nWelcome to Expedia " + name + "!\n");
        System.out.println("1. View/Edit Profile");
        if (userType.equals("admin")){
            System.out.println("2. Ban email");
            System.out.println("3. Calculate Payouts");
            System.out.println("4. View top 10 indebted users");
            System.out.println("5. Logout");
            System.out.println("6. Quit");
        }
        else {
            System.out.println("2. Plan a Trip");
            if (userType.equals("paidUser")) {
                System.out.println("3. Sell a Service");
                System.out.println("4. Logout");
                System.out.println("5. Quit");
            }
            else {
                System.out.println("3. Logout");
                System.out.println("4. Quit");
            }
        }
        for (int i = 0; i < 35; i++) System.out.print("-");
        System.out.println();
    }

    /**
     * Retrieves a menu option based on the user type and menu number.
     *
     * @param userType The type of the user (admin, paidUser, or other)
     * @param num      The number corresponding to the menu option
     * @return The corresponding MenuOptions enum value
     */
    public static MenuOptions getMenuOption(String userType, int num) {
        if (num == 1) return PROFILE;
        if (userType.equalsIgnoreCase("admin")) {
            if (num == 2) return BAN;
            if (num == 3) return PAYOUT;
            if (num == 4) return INDEBTED_USERS;
            if (num == 5) return LOGOUT;
            else return QUIT;
        }
        if (num == 2) return PLAN_TRIP;
        else if (userType.equalsIgnoreCase("paidUser")) {
            if (num == 3) return SELL;
            if (num == 4) return LOGOUT;
            else return QUIT;
        }
        else {
            if (num == 3) return LOGOUT;
            else return QUIT;
        }
    }
}
