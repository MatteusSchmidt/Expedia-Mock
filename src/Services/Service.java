package Services;
import UserHierarchy.Cost;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Base service to build POJOs off of
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */
public abstract class Service implements Cost {
    protected String location;
    protected Double cost;
    protected final LocalDate startDate;
    protected LocalDate endDate;

    /**
     * Constructs a Service object with the specified location, cost, start date, and end date.
     *
     * @param location the location of the service
     * @param cost the cost of the service
     * @param startDate the start date of the service
     * @param endDate the end date of the service
     */

    public Service(String location, double cost, LocalDate startDate, LocalDate endDate) {
        this.location = location;
        setCost(cost);
        this.startDate = startDate;
        setEndDate(endDate);
    }

    /**
     * Constructs a Service object with the specified location, start date, and end date.
     *
     * @param location the location of the service
     * @param startDate the start date of the service
     * @param endDate the end date of the service
     */
    public Service(String location, LocalDate startDate, LocalDate endDate) {
        this.location = location;
        this.startDate = startDate;
        setEndDate(endDate);
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    @Override
    public double getCost() { return cost; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) {
        if (endDate.isBefore(startDate)) throw new IllegalArgumentException("the end date must come after or on the start date");
        else this.endDate = endDate;
    }
    protected void setCost(Double cost) { this.cost = Cost.round(cost, 2);}
    public String toDBString() {
        return "[" + location + "," + cost + "," + startDate.toString() + "," + endDate.toString() + "," ;
    }

    /**
     * Reads a string representation of a service and returns the data as an ArrayList of strings.
     *
     * @param serviceDBString*/
    public static ArrayList<String> readService(String serviceDBString) {
        Scanner objectData = new Scanner(serviceDBString.replace("[", "").replace("]", ""));
        objectData.useDelimiter(",");
        ArrayList<String> data = new ArrayList<>();
        while (objectData.hasNext()) data.add(objectData.next());
        return data;
    }

    /**
     * Calculates the duration of a service in days.
     *
     * @return the duration of the service in days
     * @throws IllegalStateException if the start date or end date is null
     */
    protected int duration() {
        if (startDate == null || endDate == null) throw new IllegalStateException("Start date and end date must not be null");
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }
}
