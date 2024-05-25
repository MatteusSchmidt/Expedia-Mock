package Services;
import Services.Enums.CarType;
import Services.Enums.FlightClass;
import UserHierarchy.Cost;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

// I could've made a for each with iterator. didn't think of this until much later. sad
/**
 * POJO to encapsulate Items in the services hierarchy
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public final class Itinerary {
    public static final double RENTAL_TAX_RATE = .10;
    public static final double PLANE_TICKET_TAX_RATE = .075;
    public static final double HOTEL_TAX_RATE = .06;

    private ArrayList<Flight> flights;
    private ArrayList<RentalCar> rentalCars;
    private ArrayList<Hotel> hotels;
    private final String name;
    private final double totalCost;
    public Itinerary(String name, ArrayList<Flight> flights, ArrayList<RentalCar> rentalCars, ArrayList<Hotel> hotels) {
        this.name = name;
        this.flights = flights;
        this.rentalCars = rentalCars;
        this.hotels = hotels;
        totalCost = getCost();
    }

    /**
     * Calculates the total cost of all the services in the itinerary.
     * The cost is calculated by summing up the cost of each flight, hotel, and rental car.
     *
     * @return the total cost of the itinerary
     */
    private double getCost() {
        double totalCost = 0;
        if (flights != null && !flights.isEmpty()) {
            for (Flight flight : flights) totalCost += flight.getCost();
        }
        if (hotels != null && !hotels.isEmpty()) {
            for (Hotel hotel : hotels) totalCost += hotel.getCost();
        }
        if (rentalCars != null && !rentalCars.isEmpty()) {
            for (RentalCar rentalCar : rentalCars) totalCost += rentalCar.getCost();
        }
        return Cost.round(totalCost, 2);
    }

    public String getName() { return name; }
    public double getTotalCost() { return totalCost; }
    public ArrayList<Flight> getFlights() { return flights; }
    public void setFlights(ArrayList<Flight> flights) { this.flights = flights; }
    public ArrayList<RentalCar> getRentalCars() { return rentalCars; }
    public void setRentalCars(ArrayList<RentalCar> rentalCars) { this.rentalCars = rentalCars; }
    public ArrayList<Hotel> getHotels() { return hotels; }
    public void setHotels(ArrayList<Hotel> hotels) { this.hotels = hotels; }

    /**
     * Converts the Itinerary object into a string representation that can be stored in a database.
     * The string representation follows a specific format and includes the name of the itinerary,
     * as well as the flights, hotels, and rental cars associated with it.
     *
     * The format of the string is as follows:
     * {name,F[f1,f2,...],H[h1,h2,...],C[c1,c2,...]}
     * - name: The name of the itinerary
     * - f1, f2, ...: String representations of all flights in the itinerary (using Flight.toDBString())
     * - h1, h2, ...: String representations of all hotels in the itinerary (using Hotel.toDBString())
     * - c1, c2, ...: String representations of all rental cars in the itinerary (using RentalCar.toDBString())
     *
     * If a specific service type (flights, hotels, rental cars) is empty, it will not be included in the string.
     *
     * @return The string representation of the Itinerary object for database storage.
     */
    public String toDBString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(name);
        sb.append(",");
        if (flights != null && !flights.isEmpty()) {
            sb.append("F");
            for (Flight flight : flights) {
                sb.append(flight.toDBString());
            }
        }
        if (hotels != null && !hotels.isEmpty()) {
            sb.append("H");
            for (Hotel hotel : hotels) {
                sb.append(hotel.toDBString());
            }
        }
        if (rentalCars != null && !rentalCars.isEmpty()) {
            sb.append("C");
            for (RentalCar rentalCar : rentalCars) {
                sb.append(rentalCar.toDBString());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Converts an ArrayList of Itinerary objects into a string representation that can be stored in a database.
     * The string representation follows a specific format and includes the name of the itinerary,
     * as well as the flights, hotels, and rental cars associated with it.
     *
     * The format of the string is as follows:
     * {name,F[f1,f2,...],H[h1,h2,...],C[c1,c2,...]}
     * - name: The name of the itinerary
     * - f1, f2, ...: String representations of all flights in the itinerary (using Flight.toDBString())
     * - h1, h2, ...: String representations of all hotels in the itinerary (using Hotel.toDBString())
     * - c1, c2, ...: String representations of all rental cars in the itinerary (using RentalCar.toDBString())
     *
     * If a specific service type (flights, hotels, rental cars) is empty, it will not be included in the string.
     *
     * @param itineraries The ArrayList of Itinerary objects to be converted to a database string.
     * @return The string representation of the Itinerary objects for database storage.
     */
    public static String toDBString(ArrayList<Itinerary> itineraries) {
        StringBuilder sb = new StringBuilder();
        if (!itineraries.isEmpty()) sb.append(itineraries.get(0).toDBString());
        if (itineraries.size() > 1) {
            for (int i = 1; i < itineraries.size(); i++) {
                sb.append(",");
                sb.append(itineraries.get(i).toDBString());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-".repeat(100));
        sb.append("\n");
        sb.append("Name: ");
        sb.append(name);
        sb.append("\n\n");
        if (flights != null && !flights.isEmpty()) {
            sb.append("\tFlights\n");
            for (Flight flight : flights) {
                sb.append("\t\t");
                sb.append(flight.toString());
                sb.append("\n");
            }
            sb.append("\n");
        }
        if (hotels != null && !hotels.isEmpty()) {
            sb.append("\tHotels\n");
            for (Hotel hotel : hotels) {
                sb.append("\t\t");
                sb.append(hotel.toString());
                sb.append("\n");
            }
            sb.append("\n");
        }
        if (rentalCars != null && !rentalCars.isEmpty()) {
            sb.append("\tRental Cars\n");
            for (RentalCar rentalCar : rentalCars) {
                sb.append("\t\t");
                sb.append(rentalCar.toString());
                sb.append("\n");
            }
        }
        sb.append("\n");
        double tax = tax();
        double total = Cost.round(tax + totalCost, 2);
        sb.append("-".repeat(100));
        sb.append("\n");
        sb.append("Subtotal: $"); sb.append(totalCost);
        sb.append("\nTotal Tax: $"); sb.append(tax);
        sb.append("\nTotal: $"); sb.append(total);sb.append("\n");
        sb.append("-".repeat(100));
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Calculates the total tax for the itinerary.
     *
     * @return the total tax amount
     */
    private double tax() {
        double total = 0.0;
        total += getTotalHotelTax();
        total += getTotalPlaneTicketTax();
        total += getTotalRentalTax();
        return Cost.round(total, 2);
    }

    /**
     * Calculates the total rental tax for the itinerary.
     *
     * @return the total rental tax amount
     */
    public double getTotalRentalTax() {
        double rentalCost = 0;
        if (rentalCars != null && !rentalCars.isEmpty()) {
            for (RentalCar rentalCar : rentalCars) {
                rentalCost += rentalCar.getCost();
            }
        }
        return Cost.round(rentalCost * RENTAL_TAX_RATE, 2);
    }

    /**
     * Calculates the total tax for the plane tickets in the itinerary.
     * The total tax is calculated by summing up the cost of each flight and applying the plane ticket tax rate.
     *
     * @return the total tax amount for the plane tickets
     */
    public double getTotalPlaneTicketTax() {
        double planeCost = 0;
        if (flights != null && !flights.isEmpty()) {
            for (Flight flight : flights) {
                planeCost += flight.getCost();
            }
        }
        return Cost.round(planeCost * PLANE_TICKET_TAX_RATE, 2);
    }

    /**
     * Calculates the total tax for the hotels in the itinerary.
     *
     * @return the total hotel tax amount
     */
    public double getTotalHotelTax() {
        double hotelCost = 0;
        if (hotels != null && !hotels.isEmpty()) {
            for (Hotel hotel : hotels) {
                hotelCost += hotel.getCost();
            }
        }
        return Cost.round(hotelCost * HOTEL_TAX_RATE, 2);
    }

    /**
     * Creates an ArrayList of Itinerary objects from a string representation.
     *
     * @param itinerariesDBString The string representation of the itineraries stored in a database.
     *                            It should follow the format: {name,F[f1,f2,...],H[h1,h2,...],C[c1,c2,...]}
     *                            - name: The name of the itinerary.
     *                            - f1, f2, ...: String representations of all flights in the itinerary.
     *                              (using Flight.toDBString())
     *                            - h1, h2, ...: String representations of all hotels in the itinerary.
     *                              (using Hotel.toDBString())
     *                            - c1, c2, ...: String representations of all rental cars in the itinerary.
     *                              (using RentalCar.toDBString())
     * @return The ArrayList of Itinerary objects created from the string representation.
     *         If the input string is "null" (case-insensitive), null is returned.
     */
    public static ArrayList<Itinerary> createItineraries(String itinerariesDBString) {
        if (itinerariesDBString.equalsIgnoreCase("null")) return null;
        Scanner sc = new Scanner(itinerariesDBString);
        sc.useDelimiter("},");
        ArrayList<Itinerary> itineraryDBString = new ArrayList<>();
        while (sc.hasNext()) itineraryDBString.add(createItinerary(sc.next()));
        return itineraryDBString;
    }

    /**
     * Creates an itinerary object from a string representation of the itinerary stored in a database.
     *
     * @param itineraryDBString The string representation of the itinerary. It should follow the format:
     *                          {name,F[f1,f2,...],H[h1,h2,...],C[c1,c2,...]}
     *                          - name: The name of the itinerary.
     *                          - f1, f2, ...: String representations of all flights in the itinerary.
     *                            (using Flight.toDBString())
     *                          - h1, h2, ...: String representations of all hotels in the itinerary.
     *                            (using Hotel.toDBString())
     *                          - c1, c2, ...: String representations of all rental cars in the itinerary.
     *                            (using RentalCar.toDBString())
     * @return The created itinerary object.
     */
    public static Itinerary createItinerary(String itineraryDBString) {
        ArrayList<Flight> flights = new ArrayList<>();
        ArrayList<Hotel> hotels = new ArrayList<>();
        ArrayList<RentalCar> rentalCars = new ArrayList<>();

        itineraryDBString = itineraryDBString.replace("{", "");
        itineraryDBString = itineraryDBString.replace("]}", "");
        Scanner sc = new Scanner(itineraryDBString);
        sc.useDelimiter(",");
        String name = sc.next();
        String objectType = "";
        sc.useDelimiter("\\[");
        do {
            String temp = sc.next();
            if (!temp.isEmpty() && !temp.equals("]")) objectType = temp.replace(",", "").replace("]", "");
            sc.useDelimiter("]");
            ArrayList<String> data = Service.readService(sc.next());
            if (objectType.equals("F")) {
                flights.add(new Flight(data.get(0), LocalDate.parse(data.get(2)), LocalDate.parse(data.get(3)), data.get(4), Double.parseDouble(data.get(5)), FlightClass.getClassType(data.get(6))));
            } else if (objectType.equals("C")) {
                rentalCars.add(new RentalCar(data.get(0), LocalDate.parse(data.get(1)), LocalDate.parse(data.get(2)), CarType.getCarType(data.get(3))));
            } else if (objectType.equals("H")) {
                hotels.add(new Hotel(data.get(0), LocalDate.parse(data.get(1)), LocalDate.parse(data.get(2)), Integer.parseInt(data.get(3)), Integer.parseInt(data.get(4)), Boolean.parseBoolean(data.get(5))));
            }
            sc.useDelimiter("\\[");
        } while (sc.hasNext());
        return new Itinerary(name, flights, rentalCars, hotels);
    }

    public static void main(String[] args) {
        ArrayList<Flight> flights = new ArrayList<>();
        ArrayList<Hotel> hotels = new ArrayList<>();
        ArrayList<RentalCar> rentalCars = new ArrayList<>();
        Flight flight = new Flight("DFW", LocalDate.of(2024, 6, 12),
                LocalDate.of(2024, 6, 12), "LAX", 200, FlightClass.BUSINESS);
        Hotel hotel = new Hotel("Dallas", LocalDate.of(2024, 6, 12),
                LocalDate.of(2024, 6, 14), 4, 5, false);
        RentalCar rental = new RentalCar("DFW", LocalDate.of(2024, 6, 12),
                LocalDate.of(2024, 6, 13), CarType.COUPE);
        hotels.add(hotel);
        rentalCars.add(rental);
        Itinerary hello = new Itinerary("thing", flights, rentalCars, hotels);
        Itinerary hello2 = new Itinerary("thing2", flights, rentalCars, null);
        System.out.println(hello.toString());
        System.out.println(hello.toDBString());
        System.out.println(hello2.toString());
        System.out.println(hello2.toDBString());
        flights.add(flight);
        Itinerary hello3 = new Itinerary("thing3", flights, null, null);

        System.out.println(hello3.toString());
        System.out.println(hello3.toDBString());

        Itinerary baseHello = createItinerary(hello.toDBString());
        System.out.println(hello.toString());
        System.out.println(hello.toDBString());
        if (baseHello != null) {
            System.out.println(baseHello.toString());
            System.out.println(baseHello.toDBString());
        }
    }
}
