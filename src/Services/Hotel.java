package Services;
import Services.Enums.RoomType;
import UserHierarchy.Cost;
import java.time.LocalDate;

/**
 * POJO to represent a Hotel
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public class Hotel extends Service {
    private final RoomType roomType;
    private final int numGuests;
    private int stars;
    public Hotel(String location, LocalDate startDate, LocalDate endDate, int numGuests,
                 int stars, boolean presidential) {
        super(location, startDate, endDate);
        this.numGuests = numGuests;
        setStars(stars);
        roomType = RoomType.setRoomType(numGuests, presidential);
        cost = Cost.round(RoomType.getCost(numGuests, stars, presidential) * super.duration(), 2);
    }

    public int getNumGuests() { return numGuests; }
    private void setStars(int stars) {
        if (!(stars > 0 && stars < 6)) this.stars = 4;
        else this.stars = stars;
    }

    @Override
    public double getCost() { return cost; }
    @Override
    public String toDBString() {
        return "[" + location + "," + startDate.toString() + "," + endDate.toString() + "," + numGuests + "," + stars + "," +
                Services.Enums.RoomType.getRoomTypeStr(roomType) + "]";
    }
    @Override
    public String toString() {
        return "A " + Services.Enums.RoomType.getRoomTypeStr(roomType) + " room for "
                + numGuests + ", reserved at a " + stars + " star Hotel; from " + startDate.toString()
                + " to " + endDate.toString() +"; $" + cost;
    }

    public static void main(String[] args) {
        Hotel hotel = new Hotel("Dallas", LocalDate.of(2024, 06, 12),
                LocalDate.of(2024, 06, 13), 4, 5, false);
        System.out.println(hotel.toDBString());
        System.out.println(hotel.toString());
    }
}