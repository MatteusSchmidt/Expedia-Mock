import DBManagement.BSTree;
import DBManagement.BannedDBFunctions;
import DBManagement.UserDBFunctions;
import LoginFlow.Regex;
import Services.*;
import Services.Enums.CarType;
import Services.Enums.FlightClass;
import UserHierarchy.Admin;
import UserHierarchy.FreeUser;
import UserHierarchy.PaidUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Main System which integrates all Classes in the Project.
 * Relies solely upon the command line for user interfacing.
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/24/24
 */

public final class MainSystem {
    private boolean quit;
    private final BFF bff;
    private ArrayList<String> bannedEmails;
    private HashMap<String, String[]> airportsByCode;
    private DBManagement.BSTree btreeUID;
    private DBManagement.BSTree btreeEmail;
    private UserHierarchy.User currentUser;

    /**
     * Constructor for the MainSystem class.
     * Initializes the MainSystem object and sets up the necessary data structures.
     * Retrieves and sorts user information from the database.
     * Constructs binary search trees based on user UID and email.
     * Retrieves and stores airport information.
     * Retrieves and stores banned emails.
     */
    public MainSystem() {
        currentUser = null;
        quit = false;
        bff = new BFF();
        btreeUID = new BSTree(1);
        btreeEmail = new BSTree(2);
        ArrayList<String[]> allUsers = DBManagement.UserDBFunctions.getAllUsers(DBManagement.UserDBFunctions.getUsersByGroup());
        bannedEmails = DBManagement.BannedDBFunctions.getBannedEmails();
        airportsByCode = DBManagement.AirportFetching.getAirports();
        sortUsers(allUsers, 1);
        buildBBSTree(btreeUID, allUsers, 0, allUsers.size() - 1);
        sortUsers(allUsers, 2);
        buildBBSTree(btreeEmail, allUsers, 0, allUsers.size() - 1);
    }

    /**
     * Sorts the given list of users based on the specified index.
     *
     * @param users    the list of users to be sorted
     * @param byIndex  the index of the element in each user array to sort by
     */
    private void sortUsers(ArrayList<String[]> users, int byIndex) {
        users.sort(new Comparator<>() {
            public int compare(String[] user1, String[] user2) {
                if (byIndex == 6) {
                    double d1 = Double.parseDouble(user1[byIndex]);
                    double d2 = Double.parseDouble(user2[byIndex]);
                    return Double.compare(d1, d2);
                }
                else {
                    return user1[byIndex].compareTo(user2[byIndex]);
                }
            }
        });
    }

    /**
     * Builds a balanced binary search tree (BBSTree) using the given list of strings and inserts them into the provided BBSTree object.
     * The method uses a divide-and-conquer approach to recursively divide the list into two halves and insert the middle element into the BBSTree.
     * It then recursively builds the left subtree with elements from the lower half of the list and the right subtree with elements from the upper half of the list.
     * This process continues until the entire list is inserted into the BBSTree.
     *
     * @param btree the BBSTree object to be built
     * @param list the list of strings to be inserted into the BBSTree
     * @param lo the lower bound index of the list
     * @param hi the upper bound index of the list
     */
    private void buildBBSTree(DBManagement.BSTree btree, ArrayList<String[]> list, int lo, int hi) {
        if (hi < lo) return;
        int mid = lo + ((hi - lo) / 2);
        btree.insert(list.get(mid));

        buildBBSTree(btree, list, lo, mid - 1);
        buildBBSTree(btree, list, mid + 1, hi);
    }

    /**
     * Runs the Expedia application. It handles the main flow of the program,
     * including the login process, menu options, and user interaction.
     * The method runs in a continuous loop until the user either logs out or quits the application.
     *
     * @return: none
     */
    public void run() {
        while (currentUser == null && !quit) {
            loginFlow();
            while (currentUser != null && !quit) {
                ArrayList<String[]> usersByDebt = null;
                bff.println("");
                MenuOptions.printMenuOptions(currentUser.getUserType(), currentUser.getName());
                int choice;
                if (currentUser.getUserType().equalsIgnoreCase("admin")) {
                    airportsByCode = null;
                    choice = bff.inputRedInt(" >> ", 1, 6);
                    usersByDebt = DBManagement.UserDBFunctions.getNonAdminUsers(DBManagement.UserDBFunctions.getUsersByGroup());
                    DBManagement.BSTree bsTreeDebt = new BSTree(6);
                    sortUsers(usersByDebt, 6);
                    buildBBSTree(bsTreeDebt, usersByDebt, 0, usersByDebt.size() - 1);
                    usersByDebt = bsTreeDebt.inOrderTraversal();
                }
                else if (currentUser.getUserType().equalsIgnoreCase("paiduser")) choice = bff.inputRedInt(" >> ", 1, 5);
                else choice = bff.inputRedInt(" >> ", 1, 4);
                MenuOptions option = MenuOptions.getMenuOption(currentUser.getUserType(), choice);
                switch (option) {
                    case QUIT -> exit();
                    case LOGOUT -> logout();
                    case BAN -> banUser();
                    case SELL -> sellService();
                    case PAYOUT -> usersToPayout(usersByDebt);
                    case PROFILE -> profile();
                    case PLAN_TRIP -> planTrip();
                    case INDEBTED_USERS -> top10IndebtedUsers(usersByDebt);
                }
            }
        }
    }

    /**
     * Prints out the list of users who are indebted and need to be paid out.
     *
     * @param usersByDebt the list of users sorted by their debt
     */
    private void usersToPayout(ArrayList<String[]> usersByDebt) {
        bff.println("");
        ArrayList<String[]> users = new ArrayList<>();
        for (String[] array : usersByDebt) {
            if (Double.parseDouble(array[6]) >=0) break;
            users.add(array);
        }
        bff.println("Users Indebted to:");
        for (String[] user : users) bff.println("\t" + user[1] + ": owed $" + user[6]);
    }

    /**
     * Prints the top 10 users with the most debt.
     *
     * @param usersByDebt the list of users sorted*/
    private void top10IndebtedUsers(ArrayList<String[]> usersByDebt) {
        bff.println("");
        bff.println("Top 10 users with the most debt:");
        int smallDB = 0;
        int stop = usersByDebt.size() - 11;
        if (stop < 0) smallDB = -1 - stop;
        for (int i = usersByDebt.size() - 1; i > usersByDebt.size() - 11 + smallDB; i--) {
            if (Double.parseDouble(usersByDebt.get(i)[6]) <= 0) break;
            bff.println("\t" + usersByDebt.get(i)[1] + ": owes $" + usersByDebt.get(i)[6]);
        }
    }

    /**
     * Generates and prints the user profile.
     *
     * This method retrieves information from the `currentUser` object,
     * such as the user's name, ID, email, account type, and creation date.
     * It also determines whether the user has itineraries and sales.
     *
     * If the user is a `PaidUser`, it checks if the user has itineraries or sales.
     * If there are no itineraries or sales, it displays a corresponding message.
     * Otherwise, it asks the user if they want to view their itineraries or sales.
     *
     * If the user is a `FreeUser`, it checks if the user has itineraries.
     * If there are no itineraries, it displays a message.
     * Otherwise, it asks the user if they want to view their itineraries.
     *
     * If the user is not an admin, it checks if the user has a free account
     * without itineraries or with itineraries but without sales,
     * or if the user has both itineraries and sales.
     * It then displays a message to prompt the user to change their account type.
     *
     * Finally, it prints options for the user such as changing the account type or exiting,
     * and calls the `printProfileChoice()` method to further handle user input.
     *
     * @return void
     */
    private void profile() {
        for (int i = 0; i < 50; i ++) bff.print("-");
        bff.println("\nName: " + currentUser.getName());
        bff.println("ID: " + currentUser.getUID());
        bff.println("Email: " + currentUser.getEmail());
        bff.println("Account type: " + currentUser.getPrettyUserType());
        bff.println("Created on: " + currentUser.getCreated().toString() + "\n\n\n");
        boolean free;
        boolean itineraries;
        boolean sales;

        if (currentUser instanceof PaidUser) {
            PaidUser paidUser = (PaidUser) currentUser;
            free = false;
            if (paidUser.getItineraries() == null) {
                bff.println("No Itineraries to display!\n");
                itineraries = false;
            } else {
                bff.println("Would you like to view your itineraries?\n\tPast(1),\n\tCurrent(2)\n\tFuture(3)\n");
                itineraries = true;
            }
            if (paidUser.getSales() == null) {
                bff.println("No Sales to display!\n");
                sales = false;
            } else if (paidUser.getItineraries() == null) {
                bff.println("Would you like to view your Sales?\n\tPast(1),\n\tCurrent(2)\n\tFuture(3)\n");
                sales = true;
            } else {
                bff.println("Would you like to view your Sales?\n\tPast(4),\n\tCurrent(5)\n\tFuture(6)\n");
                sales = true;
            }
        }

        else if (currentUser instanceof FreeUser) {
            FreeUser freeUser = (FreeUser) currentUser;
            free = true;
            sales = false;
            if (freeUser.getItineraries() == null) {
                bff.println("No Itineraries to display!\n");
                itineraries = false;
            } else {
                bff.println("Would you like to view your itineraries?\n\tPast(1),\n\tCurrent(2)\n\tFuture(3)\n");
                itineraries = true;
            }
        }
        else { free = false; itineraries = false; sales = false; }

        if (!currentUser.getUserType().equalsIgnoreCase("admin")) {
            if (free && !itineraries) {
                bff.println("Change account type? (1)");
            } else if (itineraries && !sales || !itineraries && sales) {
                bff.println("Change account type? (4)");
            } else if (itineraries && sales) {
                bff.println("Change account type? (7)");
            }
        }
        bff.println("Exit (0)\n");
        for (int i = 0; i < 50; i ++) bff.print("-");
        bff.println("");
        printProfileChoice(itineraries, sales);
    }

    /**
     * Prints out the user's profile choice based on the specified options.
     *
     * @param itineraries true if the user has itineraries, false otherwise
     * @param sales true if the user has sales, false otherwise
     */
    private void printProfileChoice(boolean itineraries, boolean sales) {
        ArrayList<Itinerary> itineraryArrayList = null;
        ArrayList<Sales> salesArrayList = null;
        int input;
        if (!itineraries && !sales) {
            input = bff.inputRedInt(" >> ", 0, 1);
            if (input == 1) changeUserType();
        } else if (itineraries && !sales) {
            input = bff.inputRedInt(" >> ", 0, 4);
            if (input == 1) itineraryArrayList = getPPFItineraries(0);
            else if (input == 2) itineraryArrayList = getPPFItineraries(1);
            else if (input == 3) itineraryArrayList = getPPFItineraries(2);
            else if (input == 4) changeUserType();
        } else if (!itineraries && sales) {
            input = bff.inputRedInt(" >> ", 0, 4);
            if (input == 1) salesArrayList = getPPFSales(0);
            else if (input == 2) salesArrayList = getPPFSales(1);
            else if (input == 3) salesArrayList = getPPFSales(2);
            else if (input == 4) changeUserType();
        } else {
            input = bff.inputRedInt(" >> ", 0, 7);
            if (input == 1) itineraryArrayList = getPPFItineraries(0);
            else if (input == 2) itineraryArrayList = getPPFItineraries(1);
            else if (input == 3) itineraryArrayList = getPPFItineraries(2);
            else if (input == 4) salesArrayList = getPPFSales(0);
            else if (input == 5) salesArrayList = getPPFSales(1);
            else if (input == 6) salesArrayList = getPPFSales(2);
            else if (input == 7)  changeUserType();
        }

        if (salesArrayList != null || itineraryArrayList != null) for (int i = 0; i < 50; i ++) bff.print("*");
        bff.println("");
        if (itineraryArrayList != null) {
            bff.println("");
            for (Itinerary itinerary : itineraryArrayList) {
                bff.println(itinerary.toString());
            }
            bff.println("");
        }
        if (salesArrayList != null) {
            bff.println("");
            for (Sales sale : salesArrayList) {
                bff.println(sale.toString());
            }
            bff.println("");
        }
        if (salesArrayList == null && itineraryArrayList == null && input != 0) {
            bff.println("");
            bff.println("None to display :(");
            bff.println("");
        }
        if (salesArrayList != null || itineraryArrayList != null) for (int i = 0; i < 50; i ++) bff.print("*");
        if (input != 0) {
            bff.println("\n\n");
            profile();
        }
    }

    /**
     * Retrieves a list of Itineraries based on the specified PPF (Past, Present, Future) condition.
     *
     * @param PPF the condition to filter Itineraries
     *             0 - Past: Itineraries whose flights, hotels, and rental cars have all ended before the current date will be included
     *             1 - Present: Itineraries that have at least one flight, hotel, or rental car that is currently ongoing will be included
     *             2 - Future: Itineraries whose flights, hotels, and rental cars have all not yet started will be included
     * @return an ArrayList of Itineraries that match the specified PPF condition
     */
    private ArrayList<Itinerary> getPPFItineraries(int PPF) {
        ArrayList<Itinerary> itin = new ArrayList<>();
        for (Itinerary itinerary : ((FreeUser) currentUser).getItineraries()) {
            boolean add = true;
            if (PPF == 0) {
                for (Flight flight : itinerary.getFlights()) {
                    if (flight.getEndDate().isAfter(LocalDate.now())) add = false;
                }
                for (Hotel hotel : itinerary.getHotels()) {
                    if (hotel.getEndDate().isAfter(LocalDate.now())) add = false;
                }
                for (RentalCar car : itinerary.getRentalCars()) {
                    if (car.getEndDate().isAfter(LocalDate.now())) add = false;
                }
            }
            else if (PPF == 1) {
                add = false;
                if (itinerary.getFlights() != null && !itinerary.getFlights().isEmpty()) {
                    for (Flight flight : itinerary.getFlights()) {
                        if (flight.getEndDate().isAfter(LocalDate.now()) && flight.getStartDate().isBefore(LocalDate.now())) add = true;
                    }
                }
                if (itinerary.getHotels() != null && !itinerary.getHotels().isEmpty()) {
                    for (Hotel hotel : itinerary.getHotels()) {
                        if (hotel.getEndDate().isAfter(LocalDate.now()) && hotel.getStartDate().isBefore(LocalDate.now())) add = true;
                    }
                }
                if (itinerary.getRentalCars() != null && !itinerary.getRentalCars().isEmpty()) {
                    for (RentalCar car : itinerary.getRentalCars()) {
                        if (car.getEndDate().isAfter(LocalDate.now()) && car.getStartDate().isBefore(LocalDate.now())) add = true;
                    }
                }
            }
            else {
                if (itinerary.getFlights() != null && !itinerary.getFlights().isEmpty()) {
                    for (Flight flight : itinerary.getFlights()) {
                        if (flight.getEndDate().isBefore(LocalDate.now())) add = false;
                    }
                }
                if (itinerary.getHotels() != null && !itinerary.getHotels().isEmpty()) {
                    for (Hotel hotel : itinerary.getHotels()) {
                        if (hotel.getEndDate().isBefore(LocalDate.now())) add = false;
                    }
                }
                if (itinerary.getRentalCars() != null && !itinerary.getRentalCars().isEmpty()) {
                    for (RentalCar car : itinerary.getRentalCars()) {
                        if (car.getEndDate().isBefore(LocalDate.now())) add = false;
                    }
                }
            }
            if (add) itin.add(itinerary);
        }
        return itin;
    }

    /**
     * Retrieves a list of sales based on the specified PPF (Past, Present, Future) condition.
     *
     * @param PPF the condition to filter sales
     *            0 - Past: Sales that have ended before the current date will be included
     *            1 - Present: Sales that are currently ongoing will be included
     *            2 - Future: Sales that have not yet started will be included
     *
     * @return an ArrayList of Sales that match the specified PPF condition
     */
    private ArrayList<Sales> getPPFSales(int PPF) {
        ArrayList<Sales> sal = new ArrayList<>();
        for (Sales sale : ((PaidUser) currentUser).getSales()) {
            if (PPF == 0) {
                if (sale.getSale().getEndDate().isBefore(LocalDate.now())) sal.add(sale);
            }
            else if (PPF == 1) {
                if (sale.getSale().getEndDate().isAfter(LocalDate.now())
                        && sale.getSale().getStartDate().isBefore(LocalDate.now())) sal.add(sale);
            }
            else {
                if (sale.getSale().getStartDate().isAfter(LocalDate.now())) sal.add(sale);
            }
        }
        return sal;
    }

    /**
     * Changes the user's account type based on their current account type.
     * If the user is a PaidUser, they are given the option to decline their membership.
     * If they choose to decline, their account type is changed to a FreeUser.
     * If they choose not to decline, they are redirected to the profile page.
     *
     * If the user is a FreeUser, they are given the option to pay $20 for membership.
     * If they choose to pay, their account type is changed to a PaidUser and their debt is updated.
     * If they choose not to pay, they are redirected to the profile page.
     */
    private void changeUserType() {
        if (currentUser instanceof PaidUser) {
            if (bff.inputYesNo("Would you like to decline your membership? (Yes/No) : ")) {
                PaidUser paidUser = (PaidUser) currentUser;
                currentUser = new FreeUser("freeUser", paidUser.getUID(), paidUser.getEmail(), paidUser.getPassword(), paidUser.getName(), paidUser.getCreated(), paidUser.getDebt(), paidUser.getItineraries());
            }
            else {
                bff.println("Returning to the profile page.");
                profile();
            }
        }
        else {
            if (bff.inputYesNo("Would you like pay $20 for membership? (Yes/No) : ")) {
                FreeUser freeUser = (FreeUser) currentUser;
                double debt = freeUser.getDebt() + 20;
                currentUser = new PaidUser("paidUser", freeUser.getUID(), freeUser.getEmail(), freeUser.getPassword(), freeUser.getName(), freeUser.getCreated(), debt, freeUser.getItineraries(), null);
            }
            else {
                bff.println("Returning to the profile page.");
                profile();
            }
        }
    }

    /**
     * This method allows the user to plan a trip by inputting the necessary details such as the name of the itinerary,
     * the type of service to add (hotel, flights, or rental car), and additional services they want to add to the itinerary.
     * The method then creates the necessary objects (Hotel, Flight, or RentalCar) based on user input and adds them to the
     * corresponding ArrayLists (hotels, flights, or rentalCars). The method calculates the total cost of the itinerary and
     * creates an Itinerary object. If the current user is a PaidUser, the itinerary is added to their list of itineraries.
     * If the current user is a FreeUser, the itinerary is added to their list of itineraries. Finally, the method prints
     * a confirmation message with the name of the itinerary that was saved to the user's profile.
     */
    private void planTrip() {
        ArrayList<Flight> flights = new ArrayList<>();
        ArrayList<Hotel> hotels = new ArrayList<>();
        ArrayList<RentalCar> rentalCars = new ArrayList<>();
        String name = bff.input("What would you like to name this itinerary? : ");
        String serviceType = bff.input("What type of service would you like to add to the Itinerary? " +
                "\n(Hotel, Flights, Rental Car) : ", "Hotel", "Flights", "Rental Car").toLowerCase();
        double cost = 0.0;
        Service firstService;
        String input;
        switch (serviceType) {
            case "hotel" ->  {
                Hotel hotel = createHotel(false);
                firstService = hotel;
                hotels.add(hotel);
                cost += hotel.getCost();
                input = bff.input("Would you like to purchase a Flight or Rental Car at " + firstService.getLocation() + " on " + firstService.getStartDate() + "? : ", "flight", "rental car", "no");
            }
            case "rental car" -> {
                RentalCar rentalCar = createRentalCar(false);
                firstService = rentalCar;
                rentalCars.add(rentalCar);
                cost += rentalCar.getCost();
                input = bff.input("Would you like to purchase a Hotel or Flight at " + firstService.getLocation() + " on " + firstService.getStartDate() + "? : ", "hotel", "flight", "no");
            }
            default -> {
                ArrayList<Flight> flights2 = createFlights(false);
                firstService = flights2.get(0);
                flights.addAll(flights2);
                for (Flight flight : flights2) cost += flight.getCost();
                input = bff.input("Would you like to purchase a Hotel or Rental car at " + ((Flight) firstService).getDestination() + " on " + firstService.getStartDate() + "? : ", "hotel", "rental car", "no");
            }
        }
        String location;
        if (firstService instanceof Flight) location = ((Flight) firstService).getDestination();
        else location = firstService.getLocation();

        if (!input.equalsIgnoreCase("no")) {
            if (input.equalsIgnoreCase("hotel")) {
                Hotel hotel = createHotel(location, firstService.getStartDate(), false);
                hotels.add(hotel);
                cost += hotel.getCost();
            }
            else if (input.equalsIgnoreCase("rental car")) {
                RentalCar rentalCar = createRentalCar(location, firstService.getStartDate(), false);
                rentalCars.add(rentalCar);
                cost += rentalCar.getCost();            }
            else {
                ArrayList<Flight> flights2 = createFlights(firstService.getStartDate(), false);
                flights.addAll(flights2);
                for (Flight flight : flights2) cost += flight.getCost();
            }
        }
        while (!input.equalsIgnoreCase("save")) {
            serviceType = bff.input("Would you like to add another service to the Itinerary? (Hotel, Flights, Rental Car, Save) : ", "Hotel", "Flights", "Rental Car", "save").toLowerCase();
            switch (serviceType) {
                case "hotel" ->  hotels.add(createHotel(false));
                case "rental car" -> rentalCars.add(createRentalCar(false));
                case "flights" -> flights.addAll(createFlights(false));
                default -> input = "save";
            }
        }
        Itinerary itinerary = new Itinerary(name, flights, rentalCars, hotels);
        if (currentUser instanceof PaidUser) {
            PaidUser paidUser = (PaidUser) currentUser;
            ArrayList<Itinerary> itineraries = (paidUser.getItineraries() == null) ? new ArrayList<>() : paidUser.getItineraries();
            itineraries.add(itinerary);
            currentUser = new PaidUser(paidUser.getUserType(), paidUser.getUID(), paidUser.getEmail(), paidUser.getPassword(),
                    paidUser.getName(), paidUser.getCreated(), cost, itineraries, paidUser.getSales());
        }
        else if (currentUser instanceof FreeUser) {
            FreeUser freeUser = (FreeUser) currentUser;
            ArrayList<Itinerary> itineraries = (freeUser.getItineraries() == null) ? new ArrayList<>() : freeUser.getItineraries();
            itineraries.add(itinerary);
            currentUser = new FreeUser(freeUser.getUserType(), freeUser.getUID(), freeUser.getEmail(), freeUser.getPassword(),
                    freeUser.getName(), freeUser.getCreated(), cost, itineraries);
        }
        bff.println("Saved " + name + " to profile!\n");
    }

    /**
     * Method to sell a service.
     *
     * This method prompts the user to select the type of service they would like to sell (hotel, flights, or rental car).
     * Based on the user's selection, it creates the corresponding service object(s) and adds*/
    private void sellService() {
        String serviceType = bff.input("What type of service would you like to sell? (Hotel, Flights, Rental Car) : ", "Hotel", "Flights", "Rental Car").toLowerCase();
        Services.Service service = null;
        ArrayList<Flight> flights = null;
        PaidUser paidUser = (PaidUser) currentUser;
        switch (serviceType) {
            case "hotel" -> service = createHotel(true);
            case "rental car" -> service = createRentalCar(true);
            default -> flights = createFlights(true);
        }
        ArrayList<Sales> sales = new ArrayList<>();
        if (flights == null) {
            if (service.getClass() == Hotel.class) sales.add(new Sales((Hotel) service));
            else sales.add(new Sales((RentalCar) service));
        }
        else {
            for (Flight flight: flights) {
                sales.add(new Sales(flight));
            }
        }

        if (paidUser.getSales() != null) {
            sales.addAll(paidUser.getSales());
        }
        double payout = paidUser.getDebt();
        if (service == null) {
            for (Flight flight : flights) payout -= flight.getCost();
        }
        else payout -= service.getCost();

        currentUser = new PaidUser(paidUser.getUserType(), paidUser.getUID(), paidUser.getEmail(), paidUser.getPassword(),
                paidUser.getName(), paidUser.getCreated(), payout, paidUser.getItineraries(), sales);
    }

    /**
     * Bans a user from the system.
     *
     * @return void
     */
    private void banUser() {
        String ban = bff.input("\tPlease enter a user to ban: ");
        String error = Regex.emailRegex(ban);
        if (!error.isEmpty()) bff.printRed(error);
        else if (bannedEmails.contains(ban)) bff.printRed("\tUser already banned");
        else { // no need to delete from bst and rewrite to file as we'd like to keep their data
            bannedEmails.add(ban);
            BannedDBFunctions.writeBannedEmailsToFile(bannedEmails);
            bff.printRed("\t" + ban + " banned");
            Admin admin = (Admin) currentUser;
            ArrayList<String> bans = new ArrayList<>();
            if (admin.getBans() != null) bans.addAll(admin.getBans());
            bans.add(ban);
            admin.setBans(bans);
            currentUser = admin;
        }
    }

    /**
     * Logs out the current user.
     *
     * This method logs out the current user by resetting the `currentUser` variable to null.
     * If the current user is a `PaidUser`, it saves their sales and itineraries to the database.
     * If the current user is a `FreeUser`, it saves their itineraries to the database.
     * If the current user is an `Admin`, it saves their bans to the database.
     * After saving the data, it writes all the user records in the `btreeUID` to a file using `UserDBFunctions.writeUsersToFile` method.
     */
    private void logout() {
        if (currentUser instanceof PaidUser) {
            PaidUser user = (PaidUser) currentUser;
            String sales = (user.getSales() == null) ? "null" : Sales.toDBString(user.getSales());
            String itineraries = (user.getItineraries() == null) ? "null" : Itinerary.toDBString(user.getItineraries());
            String[] userToString = new String[] {
                    user.getUserType(),
                    user.getUID(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getName(),
                    user.getCreated().toString(),
                    Double.toString(user.getDebt()),
                    itineraries,
                    sales
            };
            btreeUID.insert(userToString);
            UserDBFunctions.writeUsersToFile(btreeUID.inOrderTraversal());
        }
        else if (currentUser instanceof FreeUser) {
            FreeUser user = (FreeUser) currentUser;
            String itineraries = (user.getItineraries() == null) ? "null" : Itinerary.toDBString(user.getItineraries());
            String[] userToString = new String[] {
                    user.getUserType(),
                    user.getUID(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getName(),
                    user.getCreated().toString(),
                    Double.toString(user.getDebt()),
                    itineraries
            };
            btreeUID.insert(userToString);
            UserDBFunctions.writeUsersToFile(btreeUID.inOrderTraversal());
        }
        else {
            Admin user = (Admin) currentUser;
            String[] userToString = new String[] {
                    user.getUserType(),
                    user.getUID(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getName(),
                    user.getCreated().toString(),
                    Admin.bansToString(user.getBans())
            };
            btreeUID.insert(userToString);
            UserDBFunctions.writeUsersToFile(btreeUID.inOrderTraversal());
        }
        currentUser = null;
    }

    /**
     * Performs the login flow for the Expedia Application.
     *
     * This method handles the initial interaction with the user, prompting them
     * to either login, create an account, or exit the application. Based on the
     * user's input, it calls the corresponding methods to perform the desired action.
     */
    private void loginFlow() {
        bff.println("Welcome to my totally awesome Expedia Application!!!");
        bff.println("This awesome project has 100 percent test coverage!!!");
        bff.println("I never even respected my own time anyways! I love my PM!\n");
        String userInput = bff.input("Do you want Login, Create Account, or Exit? ", "login", "create account", "exit").toLowerCase();
        bff.println("");

        switch (userInput) {
            case "create account": createAccount(); break;
            case "login": login(); break;
            case "exit": exit(); break;
        }
    }

    /**
     * Creates an account for the user.
     *
     * The method prompts the user for their email, and checks if the email is already registered with Expedia.
     * If the email is already registered, it prompts the user if they would like to login instead.
     **/
    private void createAccount() {
        String userEmail = emailValidation();
        if (!userEmail.isEmpty() && btreeEmail.contains(userEmail)) {
            bff.println("You are already registered with Expedia.");
            userEmail = bff.inputYesNo("Would you like to login? (Yes/No): ") ? "login" : "";
            if (userEmail.equals("login")) login();
        }
        else if (!quit) {
            String userPassword = passwordValidation();
            String[] maxUIDUser = btreeUID.getMax();
            String UID = DBManagement.UserDBFunctions.setUID(maxUIDUser[1]);
            String userName = nameValidation();
            String userType = bff.inputYesNo("Would you like to purchase a premium account for only $20?" +
                    "\n\tThis gives you access to sell your own services. (Yes/No): ") ? "paidUser" : "freeUser";
            if (userType.equalsIgnoreCase("freeUser")) {
                currentUser = new UserHierarchy.FreeUser(userType, UID, userEmail, userPassword, userName);
            }
            else {
                currentUser = new UserHierarchy.PaidUser(userType, UID, userEmail, userPassword, userName);
            }
        }
    }

    /**
     * Validates the user's name*/
    private String nameValidation() {
        String userName = bff.inputWord("What is your name? ");
        String error = Regex.nameRegex(userName);
        while (!error.isEmpty()) {
            bff.printRed(error);
            userName = bff.inputWord("What is your name? ");
            error = Regex.nameRegex(userName);
        }
        return userName;
    }

    /**
     * Validates email inputs
     */
    private String emailValidation() {
        String userEmail;
        bff.println("Please enter an Email and Password:");
        userEmail = bff.inputWord("\tEmail: ").toLowerCase();
        String emailErrorMessage = LoginFlow.Regex.emailRegex(userEmail);
        while (!emailErrorMessage.isEmpty()) {
            bff.printRed("\t" + emailErrorMessage);
            userEmail = bff.inputWord("\tEmail: ").toLowerCase();
            emailErrorMessage = LoginFlow.Regex.emailRegex(userEmail);
        }
        if (bannedEmails.contains(userEmail)) {
            bff.printRed("\nThis account is banned. Please get out of here!");
            exit();
            return "";
        }
        return userEmail;
    }

    /**
     * Validates the user password according to a specific regular expression.
     *
     * @return The encrypted user password if it passes the validation, otherwise an empty string.
     */
    private String passwordValidation() {
        String userPassword = bff.inputWord("\tPassword: ");
        String passwordErrorMessage = LoginFlow.Regex.passwordRegex(userPassword);
        while (!passwordErrorMessage.isEmpty()) {
            bff.printRed("\t" + passwordErrorMessage);
            userPassword = bff.inputWord("\tPassword: ");
            passwordErrorMessage = LoginFlow.Regex.passwordRegex(userPassword);
        }
        return LoginFlow.PasswordCryptography.passwordCryptography(userPassword);
    }

    /**
     * Logs in the user to the system.
     */
    private void login() {
        String userEmail = emailValidation();
        String[] userString = btreeEmail.getStrUser(userEmail);
        if (!quit) {
            if (userString == null) {
                if (bff.inputYesNo(userEmail + " is not a registered account, would you like to " +
                        "\n\tcreate an account? (Yes/No): ")) {
                    createAccount();
                } else loginFlow();
            } else {
                String userPassword = bff.inputWord("\tPassword: ");
                userPassword = LoginFlow.PasswordCryptography.passwordCryptography(userPassword);
                if (userPassword.equals(userString[3])) {
                    if (userString[0].equals("admin")) {
                        // userString[6]
                        currentUser = new Admin(userString[0], userString[1], userString[2], userString[3], userString[4], LocalDate.parse(userString[5]), Admin.createBans(userString[6]));
                    } else if (userString[0].equals("freeUser")) { //TODO
                        // userString[7], userString[8]
                        currentUser = new FreeUser(userString[0], userString[1], userString[2], userString[3], userString[4], LocalDate.parse(userString[5]), Double.parseDouble(userString[6]), Itinerary.createItineraries(userString[7]));
                    } else {
                        currentUser = new PaidUser(userString[0], userString[1], userString[2], userString[3], userString[4], LocalDate.parse(userString[5]), Double.parseDouble(userString[6]), Itinerary.createItineraries(userString[7]), Sales.createSales(userString[8]));
                    }
                    bff.println("Welcome back " + userString[4] + "!");
                } else {
                    bff.printRed("Incorrect Email or Password");
                    if (bff.inputYesNo("Would you like to try again? (Yes/No) : ")) {
                        login();
                    } else loginFlow();
                }
            }
        }
    }

    /**
     * Creates a hotel with the specified location and start date.
     *
     * @param sell Specifies whether the hotel is for selling or booking.
     * @return The created hotel.
     */
    private Services.Hotel createHotel(boolean sell) {
        String dest = sell ? bff.inputWord("Where is your Hotel Booking located? : ") :
                bff.inputWord("Where would you like to book the Hotel? : ");
        LocalDate startDate = getInitialDate();
        return createHotel(dest, startDate, sell);
    }

    /**
     * Creates a new hotel reservation.
     *
     * @param dest The destination of the hotel.
     * @param startDate The start date of the reservation.
     * @param sell Flag indicating if the reservation is for selling or booking.
     * @return The created hotel reservation.
     */
    private Services.Hotel createHotel(String dest, LocalDate startDate, boolean sell) {
        LocalDate endDate = sell ? startDate.plusDays(bff.inputRedInt("How many nights is your booking? : ", 1, 30)) :
                startDate.plusDays(bff.inputRedInt("How many nights will you stay? : ", 1, 30));
        int numGuests = bff.inputRedInt("For how many guests? : ", 1, 200);
        int stars = sell ? bff.inputRedInt("How many stars is your booking? (1-5): ", 1, 5) :
                bff.inputRedInt("How many stars would you like the hotel to be? (1-5): ", 1, 5);
        boolean presidential;
        if (stars < 4) presidential = false;
        else presidential = sell ? bff.inputYesNo("Is it a Presidential Suite? (Yes/No): ") :
                bff.inputYesNo("Would you like the Presidential Suite? (Yes/No): ");
        return new Hotel(dest, startDate, endDate, numGuests, stars, presidential);
    }

    /**
     * Creates a rental car.
     *
     * @param sell A flag indicating whether the car will be sold or booked.
     **/
    private Services.RentalCar createRentalCar(boolean sell) {
        String dest = sell ? bff.inputWord("Where will you be renting from? : ") : bff.inputWord("Where would you like to book your rental car? : ");
        LocalDate startDate = getInitialDate();
        return createRentalCar(dest, startDate, sell);
    }

    /**
     * Creates a rental car object with the specified destination, start date, and rental/selling status.
     *
     * @param dest the destination for the rental car
     * @param startDate the start date for the rental car
     * @param sell true if the car is being sold, false if it is being rented
     * @return a new RentalCar object
     */
    private Services.RentalCar createRentalCar(String dest, LocalDate startDate, boolean sell) {
        LocalDate endDate = sell ? bff.inputFutureDate("When would you like to rent to? : ", startDate) : bff.inputFutureDate("When would you like to book to? : ", startDate);
        CarType carType = sell ? CarType.getCarType(bff.input("What type of car are you renting?\n\tCoupe,\n\tSedan,\n\tSUV,\n\tTruck,\n\tMinivan,\n\tLimousine,\n\tSports car\n"))
                : CarType.getCarType(bff.input("What type of car would you like to purchase?\n\tCoupe,\n\tSedan,\n\tSUV,\n\tTruck,\n\tMinivan\n\tLimousine,\n\tSports car\n"));
        return new RentalCar(dest, startDate, endDate, carType);
    }

    /**
     * Retrieves the initial date for booking.
     *
     * @return the initial date for booking
     */
    private LocalDate getInitialDate() { return bff.inputFutureDate("Input the date of booking", LocalDate.now()); }

    /**
     * Retrieves the IATA identifier or airport code for the destination.
     *
     * @param from the IATA identifier or airport code for the departing location
     * @param sell a boolean value indicating if the method is used for selling or not
     *             - true: used for selling, prompt message will ask for destination
     *             - false: used for other purposes, prompt message will ask for flying destination
     * @return the IATA identifier or airport code for the destination
     */
    private String IATAdest(String from, boolean sell) {
        String to = sell ? bff.inputWord("Destination? (Enter IATA identifier / airport code): ").toUpperCase()
                : bff.inputWord("Where will you be flying to? (Enter IATA identifier / airport code): ").toUpperCase();
        while (!airportsByCode.containsKey(to) || to.equals(from)) {
            if (to.equals(from)) bff.printRed("Enter a destination different from your departing location.");
            else bff.printRed("\tNot a valid code.");
            to = bff.inputWord("Arriving at (Enter IATA identifier / airport code): ").toUpperCase();
        }
        bff.println("");
        return to;
    }

    /**
     * Retrieves the IATA code for the departing airport.
     *
     * @param sell boolean value indicating whether the method is used for selling or buying ticket
     * @return the IATA code of the departing airport
     */
    private String IATAdepart(boolean sell) {
        String from = sell ? bff.inputWord("What is the departing airport? (Enter IATA identifier / airport code): ").toUpperCase()
                : bff.inputWord("Where will you be departing from? (Enter IATA identifier / airport code): ").toUpperCase();
        while (!airportsByCode.containsKey(from)) {
            bff.printRed("\tNot a valid code.");
            from = bff.inputWord("Departing from (Enter IATA identifier / airport code): ").toUpperCase();
        }
        return from;
    }

    /**
     * Creates flights based on the given date and sell flag.
     *
     * @param date The date of the flights.
     * @param sell Indicates whether the flights are being sold (true) or being booked (false).
     * @return An ArrayList of Flight objects representing the created flights.
     */
    private ArrayList<Services.Flight> createFlights(LocalDate date, boolean sell) {
        ArrayList<Flight> flights = new ArrayList<>();
        String from = IATAdepart(sell);
        String to = IATAdest(from, sell);

        String[] destination = airportsByCode.get(to);
        String[] departingFrom = airportsByCode.get(from);

        boolean international = !destination[2].equals(departingFrom[2]);
        double[] destLatLong = getLatLong(destination[1]);
        double[] depLatLong = getLatLong(departingFrom[1]);
        double distance = DBManagement.AirportFetching.calculateDistanceMiles(depLatLong[0], depLatLong[1], destLatLong[0], destLatLong[1]);

        String flightClass;
        Boolean roundTrip = null;
        int tickets = sell ? bff.inputRedInt("How many tickets would you like to sell? : ", 1, 8) : bff.inputRedInt("How many tickets would you like to purchase? : ", 1, 8);
        String str;
        if (tickets == 1) str = "Is your ticket";
        else str = "Are your tickets";
        do {
            if (distance >= 2000 && international) flightClass = sell ? bff.input(str + " Economy, Premium Economy, Business, or First Class?" +
                    "\nChoose from: Economy, Premium Economy, Business Class, International First Class : ", "Economy", "Premium Economy", "Business Class", "International First Class") : bff.input("Would you like to book Economy, Premium Economy, Business, or First Class?" +
                        "\nChoose from: Economy, Premium Economy, Business Class, International First Class : ", "Economy", "Premium Economy", "Business Class", "International First Class");
            else flightClass = sell ? bff.input(str +" Economy, Premium Economy, or First Class?" +
                    "\nChoose from: Economy, Premium Economy, First Class : ", "Economy", "Premium Economy", "First Class"): bff.input("Would you like to book Economy, Premium Economy, or First Class?" +
                    "\nChoose from: Economy, Premium Economy, First Class : ", "Economy", "Premium Economy", "First Class");
            bff.println("");
            if (roundTrip == null) {
                roundTrip = sell ? bff.inputYesNo("Are you selling round trip? : ") : bff.inputYesNo("Would you like to book round trip? : ");
                for (int i = 0; i < tickets; i++) flights.add(new Flight(departingFrom[0], date, date, destination[0], distance, FlightClass.getClassType(flightClass)));
            }
            else {
                LocalDate date2 = bff.inputFutureDate("What date are the returning tickets on?", date);
                for (int i = 0; i < tickets; i++) flights.add(new Flight(destination[0], date2, date2, departingFrom[0], distance, FlightClass.getClassType(flightClass)));
                roundTrip = false;
            }
        } while (roundTrip);
        return flights;
    }

    /**
     * Creates a list of flights based on the given sell parameter.
     *
     * @*/
    private ArrayList<Services.Flight> createFlights(boolean sell) {
        LocalDate date = getInitialDate();
        return createFlights(date, sell);
    }

    /**
     * Retrieves the latitude and longitude values from the given string.
     *
     * @param latLong the string containing the latitude and longitude values separated by a space
     * @return an array containing the latitude and longitude values
     */
    private double[] getLatLong(String latLong) {
        Scanner sc = new Scanner(latLong);
        sc.useDelimiter(" ");
        return new double[] {sc.nextDouble(), sc.nextDouble()};
    }

    /**
     * Exits the program by logging out the current user (if any) and setting the quit
     */
    private void exit() {
        if (currentUser != null) logout();
        quit = true;
    }

    /**
     * This is the entry point for the application. It creates an instance of
     * the MainSystem class and calls the run method to start the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainSystem Expedia = new MainSystem();
        Expedia.run();
    }
}
