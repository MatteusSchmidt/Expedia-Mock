package Services;

import Services.Enums.CarType;
import Services.Enums.FlightClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A sale which contains any type of POJO
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/25/24
 */
public final class Sales {
    private final Hotel hotel;
    private final Flight flight;
    private final RentalCar car;
    private final double payToUser;
    private final String saleType;
    private final LocalDate salesDate;
    public Sales(Hotel hotel) {
        this.hotel = hotel;
        flight = null;
        car = null;
        salesDate = LocalDate.now();
        saleType = "hotel";
        payToUser = hotel.getCost();
    }
    public Sales(Flight flight) {
        this.flight = flight;
        hotel = null;
        car = null;
        salesDate = LocalDate.now();
        saleType = "flight";
        payToUser = flight.getCost();
    }
    public Sales(RentalCar rentalCar) {
        this.car = rentalCar;
        flight = null;
        hotel = null;
        salesDate = LocalDate.now();
        saleType = "rental car";
        payToUser = rentalCar.getCost();
    }

    /**
     * Represents a sales transaction.
     *
     * @param object The service object associated with the sale. Can be a Hotel, Flight, or RentalCar object.
     * @param payToUser The amount to be paid to the user for the sale.
     * @param saleType The type of the sale (hotel, flight, rental car).
     * @param salesDate The date of the sale.
     */
    public Sales(Service object, double payToUser, String saleType, LocalDate salesDate) {
        if (object.getClass() == Hotel.class) {
            hotel = (Hotel) object;
            flight = null; car = null;
        }
        else if (object.getClass() == Flight.class) {
            flight = (Flight) object;
            hotel = null; car = null;
        }
        else {
            car = (RentalCar) object;
            flight = null; hotel = null;
        }
        this.payToUser = payToUser;
        this.saleType = saleType;
        this.salesDate = salesDate;
    }

    /**
     * Retrieves the sale associated with the current Sales object.
     * The method checks if the flight, hotel, or car field is not null,
     * and returns the corresponding Service object. If none of the fields
     * are set, it returns null.
     *
     * @return the Service object associated with the current Sales object,
     *         or null if no sale is set
     */
    public Service getSale() {
        if (flight != null) return flight;
        else if (hotel != null) return hotel;
        else return car;
    }

    public LocalDate getSalesDate() { return salesDate; }

    /**
     * Creates an ArrayList of Sales objects from the given salesDBString.
     *
     * @param salesDBString the string representing the sales database
     * @return the ArrayList of Sales objects, or null if the salesDBString is "null"
     */
    public static ArrayList<Sales> createSales(String salesDBString) {
        if (salesDBString.equalsIgnoreCase("null")) return null;
        Scanner sc = new Scanner(salesDBString);
        sc.useDelimiter("},");
        ArrayList<Sales> saleDBString = new ArrayList<>();
        while (sc.hasNext()) saleDBString.add(createSale(sc.next()));
        return saleDBString;
    }

    /**
     * Creates a Sales object from the given saleDBString.
     *
     * @param saleDBString the string representing the sale
     * @return the created Sales object
     */
    public static Sales createSale(String saleDBString) {
        saleDBString = saleDBString.replace("{", "").replace("}", "");
        Scanner sc = new Scanner(saleDBString);
        sc.useDelimiter("]");
        ArrayList<String> data = Service.readService(sc.next());
        sc.useDelimiter(",");
        sc.next();
        double payToUser = sc.nextDouble();
        String saleType = sc.next();
        LocalDate salesDate = LocalDate.parse(sc.next());
        Service service = getService(saleType, data);
        return new Sales(service, payToUser, saleType, salesDate);
    }

    /**
     * Creates a Service object based on the sale type and data provided.
     *
     * @param saleType the type of the sale (hotel, flight, rental car)
     * @param data the data required to create the service object
     * @return the created Service object
     */
    private static Service getService(String saleType, ArrayList<String> data) {
        Service service;
        if (saleType.equalsIgnoreCase("hotel")) {
            service = new Hotel(data.get(0), LocalDate.parse(data.get(1)), LocalDate.parse(data.get(2)), Integer.parseInt(data.get(3)), Integer.parseInt(data.get(4)), Boolean.parseBoolean(data.get(5)));
        }
        else if (saleType.equalsIgnoreCase("flight")) {
            service = new Flight(data.get(0), LocalDate.parse(data.get(2)), LocalDate.parse(data.get(3)), data.get(4), Double.parseDouble(data.get(5)), FlightClass.getClassType(data.get(6)));
        }
        else {
            service = new RentalCar(data.get(0), LocalDate.parse(data.get(1)), LocalDate.parse(data.get(2)), CarType.getCarType(data.get(3)));
        }
        return service;
    }

    public String toDBString() {
        if (hotel != null) return "{" + hotel.toDBString() + "," + payToUser + "," + saleType + "," + salesDate.toString() + "}";
        else if (car != null) return "{" + car.toDBString() + "," + payToUser + "," + saleType + "," + salesDate.toString() + "}";
        else return "{" + flight.toDBString() + "," + payToUser + "," + saleType + "," + salesDate.toString() + "}";
    }
    public static String toDBString(ArrayList<Sales> sales) {
        StringBuilder sb = new StringBuilder();
        if (!sales.isEmpty()) sb.append(sales.get(0).toDBString());
        if (sales.size() > 1) {
            for (int i = 1; i < sales.size(); i++) {
                sb.append(",");
                sb.append(sales.get(i).toDBString());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() { return payToUser + " payout for the " + saleType + " on " + salesDate.toString(); }

    public static void main(String[] args) {
        Flight flight = new Flight("DFW", LocalDate.of(2026, 06, 12),
                LocalDate.of(2026, 06, 12), "LAX", 1200, FlightClass.ECONOMY);
        Sales sale = new Sales(flight);
        System.out.println(sale.toDBString());
        Sales sale2 = createSale(sale.toDBString());
        System.out.println(sale2.toDBString());
    }
}
