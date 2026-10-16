
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    public static void registerUser(
            String name,
            String email,
            String password) {

        String sql =
                "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Hash the password before storing it
            String hashedPassword = PasswordUtil.hashPassword(password);

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, hashedPassword);

            statement.executeUpdate();

            System.out.println("User registered successfully!");

        } catch (SQLException e) {

            // MySQL error 1062 means a duplicate entry
            if (e.getErrorCode() == 1062) {
                System.out.println(
                    "This email is already registered. " +
                    "Please use a different email."
                );
            } else {
                System.out.println("User registration failed!");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("User registration failed!");
            e.printStackTrace();
        }
    }
}