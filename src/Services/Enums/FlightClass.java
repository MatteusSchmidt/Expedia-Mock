package Services.Enums;

/**
 * Enums to represent flight class
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/29/24
 */
public enum FlightClass {
    ECONOMY(1.0),
    PREMIUM_ECONOMY(1.3),
    FIRST_SHORT_DISTANCE(2.1),
    FIRST_LONG_DISTANCE(4.2),
    BUSINESS(2.2);
    private final double multiplier;
    private FlightClass(double multiplier) { this.multiplier = multiplier; }

    public double getMultiplier() { return multiplier; }

    /**
     * Retrieves the class type as a string for a given flight class.
     *
     * @param flightClass the flight class
     * @return the class type as a string
     */
    public static String getClassTypeStr(FlightClass flightClass) {
        return switch (flightClass) {
            case PREMIUM_ECONOMY -> "Premium Economy";
            case BUSINESS -> "Business Class";
            case FIRST_SHORT_DISTANCE -> "First Class";
            case FIRST_LONG_DISTANCE -> "International First Class";
            default -> "Economy";
        };
    }

    /**
     * Retrieves the FlightClass enum value based on the provided string.
     *
     * @param string the string representing the class type
     * @return the corresponding FlightClass enum value
     */
    public static FlightClass getClassType(String string) {
        return switch (string.toLowerCase()) {
            case "premium economy" -> PREMIUM_ECONOMY;
            case "business class" -> BUSINESS;
            case "first class" -> FIRST_SHORT_DISTANCE;
            case "international first class" -> FIRST_LONG_DISTANCE;
            default -> ECONOMY;
        };
    }
}
