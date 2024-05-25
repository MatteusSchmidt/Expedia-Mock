package DBManagement;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileInputStream;
import java.util.HashMap;

/**
 * Gets and stores certain values from airports.csv
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/29/24
 */

public class AirportFetching {
    private static final String LONGPATH = "/Users/schmidt/IdeaProjects/ITP265_Spring_2024/Final Project/airports.csv";

    /**
     * Retrieves and returns a HashMap with airport information.
     * The key of the HashMap is the airport code, and the value is an array of Strings containing the following information:
     * 1. Airport name
     * 2. Latitude and longitude coordinates (in the format "latitude,longitude")
     * 3. Country code
     *
     * @return a HashMap with airport information
     */
    public static HashMap<String, String[]> getAirports() {
        HashMap<String, String[]> airports = new HashMap<>();
        try {
            Scanner sc = new Scanner(new FileInputStream(LONGPATH));
            sc.nextLine();
            while (sc.hasNextLine()) {
                Scanner line = new Scanner(sc.nextLine());
                line.useDelimiter(",");
                String airportCode = line.next();
                line.next();
                String airport = line.next();
                line.next();
                String countryCode = line.next();
                String latLong = line.next().replace("POINT (", "").replace(")", "");
                airports.put(airportCode, new String[]{airport, latLong, countryCode});
            }
        } catch (FileNotFoundException e) { System.out.println("Incorrect pathing to airports.csv; change LONGPATH in AirportFetching.java"); }
        return airports;
    }

    /**
     * Calculates the haversine distance for a given value.
     *
     * **Used StackOverflow for aid**
     *
     * @param value the value to calculate the haversine distance for
     * @return the calculated haversine distance
     */
    private static double haversineDistance(double value) {
        return Math.pow(Math.sin(value / 2), 2);
    }

    /**
     * Calculates the distance in miles between two sets of latitude and longitude coordinates.
     *
     * **Used StackOverflow for aid**
     *
     * @param lat1 The latitude of the first coordinates.
     * @param log1 The longitude of the first coordinates.
     * @param lat2 The latitude of the second coordinates.
     * @param log2 The longitude of the second coordinates.
     * @return The distance in miles between the two sets of coordinates.
     */
    public static double calculateDistanceMiles(double lat1, double log1, double lat2, double log2) {
        double dLat = Math.toRadians((lat2 - lat1));
        double dLong = Math.toRadians((log2 - log1));
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = haversineDistance(dLat) + Math.cos(lat1) * Math.cos(lat2) * haversineDistance(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // converting to miles
        return 0.621371 * 6371 * c;
    }
}
