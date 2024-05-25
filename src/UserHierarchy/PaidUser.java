package UserHierarchy;
import Services.Itinerary;
import Services.Sales;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Paid version of User
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public class PaidUser extends FreeUser {
    private ArrayList<Sales> sales;
    public PaidUser(String userType, String UID, String email, String password, String name, LocalDate created, double debt, ArrayList<Itinerary> itineraries, ArrayList<Sales> sales) {
        super(userType, UID, email, password, name, created, debt, itineraries);
        setSales(sales);
    }
    public PaidUser(String userType, String UID, String email, String password, String name) {
        super(userType, UID, email, password, name);
        itineraries = null;
        this.debt = 20;
        sales = null;
    }
    private String salesToDBString() {
        if (sales == null) return "null";
        return Sales.toDBString(sales);
    }
    public ArrayList<Sales> getSales() { return sales; }
    public void setSales(ArrayList<Sales> sales) { this.sales = sales; }
}
