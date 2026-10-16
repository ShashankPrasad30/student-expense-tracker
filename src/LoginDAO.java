
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    public static int loginUser(String email, String password) {

        String sql =
            "SELECT id, name, password FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            int userId = -1;
            String name = null;
            String storedPassword = null;

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                    name = resultSet.getString("name");
                    storedPassword = resultSet.getString("password");
                }
            }

            if (userId != -1
                    && PasswordUtil.verifyPassword(
                        password, storedPassword)) {

                // Upgrade an older SHA-256 hash after successful login
                if (PasswordUtil.isLegacyHash(storedPassword)) {

                    String newHash = PasswordUtil.hashPassword(password);

                    String updateSQL =
                        "UPDATE users SET password = ? WHERE id = ?";

                    try (PreparedStatement updateStatement =
                            connection.prepareStatement(updateSQL)) {

                        updateStatement.setString(1, newHash);
                        updateStatement.setInt(2, userId);
                        updateStatement.executeUpdate();
                    }
                }

                System.out.println("Login Successful!");
                System.out.println("Welcome, " + name);

                return userId;
            }

            System.out.println("Invalid email or password.");
            return -1;

        } catch (Exception e) {
            System.out.println("Login Failed!");
            e.printStackTrace();
            return -1;
        }
    }
}