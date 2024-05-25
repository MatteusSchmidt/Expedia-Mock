package Services;
import Services.Enums.CarType;
import UserHierarchy.Cost;
import java.time.LocalDate;

/**
 * POJO to represent a Rental Car
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public final class RentalCar extends Service {
    private final CarType carType;
    public RentalCar(String location, LocalDate startDate, LocalDate endDate, CarType carType) {
        super(location, startDate, endDate);
        this.carType = carType;
        setCost(Cost.round(this.carType.getPricePerWeek(), 2));
    }
    public CarType getCarType() { return carType; }

    @Override
    protected void setCost(Double costPerWeek) { cost = Cost.round((costPerWeek / 7) * (super.duration() + 1), 2); }
    @Override
    public String toDBString() {
        return "[" + location + "," + startDate.toString() + "," + endDate.toString() + "," + Services.Enums.CarType.getCarTypeStr(carType) + "]";
    }
    @Override
    public String toString() {
        return Services.Enums.CarType.getCarTypeStr(carType) + " reservation; from " + startDate.toString() + " to " + endDate.toString() +"; $" + cost;
    }

    public static void main(String[] args) {
        RentalCar rental = new RentalCar("DFW", LocalDate.of(2024, 6, 12),
                LocalDate.of(2024, 7, 12), CarType.getCarType("Coupe"));
        System.out.println(rental.toDBString());
        System.out.println(rental.toString());
    }
}
