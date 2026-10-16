
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection.isValid(2)) {
                System.out.println("MySQL Connection Successful!");
            } else {
                System.out.println("MySQL Connection is not valid.");
            }

        } catch (SQLException e) {
            System.out.println("MySQL Connection Failed!");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
