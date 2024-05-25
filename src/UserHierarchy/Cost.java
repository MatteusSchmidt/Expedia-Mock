package UserHierarchy;

/**
 * Cost which represents cost to the company and cost of services
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/25/24
 */
public interface Cost {
    public double getCost();

    /**
     * Rounds a double value to the specified number of decimal places.
     *
     * @param value the value to round
     * @param places the number of decimal places to round to
     * @return the rounded value
     * @throws IllegalArgumentException if places is negative
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
