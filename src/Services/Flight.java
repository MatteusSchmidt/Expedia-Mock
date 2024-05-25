package Services;
import Services.Enums.FlightClass;
import UserHierarchy.Cost;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * POJO to represent a flight
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public class Flight extends Service {
    private final FlightClass flightClass;
    private final String destination;
    private final double distance;
    public Flight(String location, LocalDate startDate, LocalDate endDate, String destination, double distance, FlightClass flightClass) {
        super(location, startDate, endDate);
        this.destination = destination;
        this.distance = distance;
        this.flightClass = flightClass;
        this.cost = setCost();
    }
    public Flight(String location, LocalDate startDate, LocalDate endDate, String destination, double distance, FlightClass flightClass, double cost) {
        this(location, startDate, endDate, destination, distance, flightClass);
        this.cost = cost;
    }
    public String getDestination() { return destination; }
    public FlightClass getFlightClass() { return flightClass; }
    public double getDistance() { return distance; }

    /**
     * Calculates the cost of a flight based on the distance, flight class, and time until the start date.
     *
     * @return the cost of the flight
     */
    public double setCost() {
        int daysBetween = (int) ChronoUnit.DAYS.between(LocalDate.now(), startDate);
        double timeMultiplier;
        if (daysBetween < 365) timeMultiplier = (1 - ((double) daysBetween / 730));
        else timeMultiplier = .5;
        return Cost.round((distance * 0.1768 * flightClass.getMultiplier() * timeMultiplier) + 50, 2);
    }

    @Override
    public String toDBString() {
        return super.toDBString() + destination + "," + distance + "," + FlightClass.getClassTypeStr(flightClass) + "]";
    }

    @Override
    public String toString() {
        return location + " to " + destination + " on " + FlightClass.getClassTypeStr(flightClass) + "; on " + startDate + "; $" + cost;
    }

    public static void main(String[] args) {
        Flight flight = new Flight("DFW", LocalDate.of(2026, 06, 12),
                LocalDate.of(2026, 06, 12), "LAX", 1200, FlightClass.ECONOMY);
        System.out.println(flight.toString());
        Flight flight2 = new Flight("DFW", LocalDate.of(2025, 4, 12),
                LocalDate.of(2025, 4, 12), "LAX", 1200, FlightClass.ECONOMY);
        System.out.println(flight2.toString());
    }
}
