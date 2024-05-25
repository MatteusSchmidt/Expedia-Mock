package Services.Enums;

/**
 * Enums to represent carType
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */
public enum CarType {
    COUPE(800),
    SEDAN(500),
    SUV(600),
    TRUCK(650),
    MINIVAN(600),
    LIMOUSINE(3000),
    SPORTSCAR(5000);
    private final double pricePerWeek;
    CarType(double pricePerWeek) {
        this.pricePerWeek = pricePerWeek;
    }
    public double getPricePerWeek() { return pricePerWeek; }

    /**
     * Returns the string representation of the given CarType.
     *
     * @param carType the CarType to get the string representation for
     * @return the string representation of the CarType
     */
    public static String getCarTypeStr(CarType carType) {
        return switch (carType) {
            case COUPE -> "Coupe";
            case SEDAN -> "Sedan";
            case SUV -> "SUV";
            case TRUCK -> "Truck";
            case MINIVAN -> "Minivan";
            case LIMOUSINE -> "Limousine";
            default -> "Sports car";
        };
    }

    /**
     * Retrieves the CarType enum value associated with the given string representation.
     *
     * @param string the string representation of the CarType
     * @return the CarType enum value
     */
    public static CarType getCarType(String string) {
        return switch (string.toLowerCase()) {
            case "coupe" -> COUPE;
            case "sedan" -> SEDAN;
            case "suv" -> SUV;
            case "truck" -> TRUCK;
            case "minivan" -> MINIVAN;
            case "limousine" -> LIMOUSINE;
            default -> SPORTSCAR;
        };
    }
}
