package UserHierarchy;
import java.time.LocalDate;

/**
 * Base class for the User hierarchy
 *
 * @author Matteus Schmidt
 * ITP 265, SPRING 24', Coffee
 * Email: maschmid@usc.edu
 * Date Created: 4/23/24
 */

public abstract class User {
    private String userType;
    private final String UID;
    private final String email;
    private String password;
    private final String name;
    private final LocalDate created;
    public User(String userType, String UID, String email, String password, String name, LocalDate created) {
        this.userType = userType;
        this.email = email;
        // in case you're offline
        this.password = password;
        this.name = name;
        this.UID = UID;
        this.created = created;
    }
    public User(String userType, String UID, String email, String password, String name) {
        this.userType = userType;
        this.email = email;
        // in case you're offline
        this.password = password;
        this.name = name;
        this.UID = UID;
        this.created = LocalDate.now();
    }

    public String getUserType() { return userType; }
    public String getPrettyUserType() {
        if (userType.equalsIgnoreCase("paiduser")) return "Paid User";
        if (userType.equalsIgnoreCase("freeuser")) return "Free User";
        else return "Admin";
    }
    public String getUID() { return UID; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public LocalDate getCreated() { return created; }
    @Override
    public String toString() {
        return userType + ", " +
                email + ", " +
                password + ", " +
                name + ", " +
                created;
    }
}
