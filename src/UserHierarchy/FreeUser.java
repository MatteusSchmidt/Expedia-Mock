package UserHierarchy;
import Services.Itinerary;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Free version of User
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public class FreeUser extends User {
    protected ArrayList<Itinerary> itineraries;
    protected double debt;
    public FreeUser(String userType, String UID, String email, String password, String name, LocalDate created, double debt, ArrayList<Itinerary> itineraries) {
        super(userType, UID, email, password, name, created);
        setItineraries(itineraries);
        this.debt = debt;
    }

    public FreeUser(String userType, String UID, String email, String password, String name) {
        super(userType, UID, email, password, name);
        itineraries = null;
        this.debt = 0;
    }
    public ArrayList<Itinerary> getItineraries() { return itineraries; }
    public double getDebt() { return debt; }
    public void setItineraries(ArrayList<Itinerary> itineraries) { this.itineraries = itineraries; }
    private String itinerariesToDBString() {
        if (itineraries == null) return "null";
        return Itinerary.toDBString(itineraries);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + debt + ", " + itinerariesToDBString();
    }

    public static void main(String[] args) {
        FreeUser user = new FreeUser("admin", "UID000000001", "maschmid@usc.edu", "ASDLFJH13984", "Matteus");

    }
}
