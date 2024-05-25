package Services.Enums;

/**
 * Enums to represent hotel types
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/25/24
 */

public enum RoomType {
    SINGLE(100.00),
    DOUBLE(112.00),
    TRIPLE(130.00),
    PRESIDENTIAL(3560.00);
    private double cost;
    private final static double ONESTAR = 1.75;
    private final static double TWOSTAR = 1.23;
    private final static double THREESTAR = 2.1;
    private final static double FOURSTAR = 2.68;
    private final static double FIVESTAR = 3.28;

    private RoomType(double cost) { this.cost = cost; }

    /**
     * Determines the type of room based on the number of guests and whether it is presidential or not.
     *
     * @param numGuests the number of guests staying in the room
     * @param presidential true if the room is presidential, false otherwise
     * @return the RoomType of the room
     */
    public static RoomType setRoomType(int numGuests, boolean presidential) {
        if (presidential) return PRESIDENTIAL;
        if (numGuests <= 2) return SINGLE;
        else if (numGuests <= 4) return DOUBLE;
        return TRIPLE;
    }

    /**
     * Calculates the cost of a hotel room based on the number of guests,
     * the star rating, and whether it is a presidential suite or not.
     *
     * @param numGuests the number of guests staying in the room
     * @param stars the star rating of the hotel
     * @param presidential true if the room is a presidential suite, false otherwise
     * @return the cost of the hotel room
     */
    public static double getCost(int numGuests, int stars, boolean presidential) {
        if (presidential) return PRESIDENTIAL.cost;
        double price;
        if (numGuests <= 2) price = SINGLE.cost;
        else if (numGuests <= 4) price = DOUBLE.cost;
        else price = TRIPLE.cost;

        if (stars == 1) price *= ONESTAR;
        if (stars == 2) price *= TWOSTAR;
        if (stars == 3) price *= THREESTAR;
        if (stars == 4) price *= FOURSTAR;
        if (stars == 5) price *= FIVESTAR;
        return price;
    }

    /**
     * Converts a string representation of a room type to the corresponding RoomType enum value.
     *
     * @param roomType a string representation of a room type ("single", "double", "triple" or "presidential")
     * @return the RoomType enum value corresponding to the given roomType string
     */
    public RoomType strToRoomType(String roomType) {
        return switch (roomType.toLowerCase()) {
            case "single" -> SINGLE;
            case "double" -> DOUBLE;
            case "triple" -> TRIPLE;
            default -> PRESIDENTIAL;
        };
    }

    public static String getRoomTypeStr(RoomType roomType) {
        return switch (roomType) {
            case SINGLE -> "Single";
            case DOUBLE -> "Double";
            case TRIPLE -> "Triple";
            default -> "Presidential suite";
        };
    }
}
