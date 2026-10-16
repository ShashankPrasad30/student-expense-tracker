import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BudgetDAO {

    public static void setBudget(
            int userId,
            double amount,
            int month,
            int year) {

        String sql =
                "INSERT INTO budgets (user_id, amount, month, year) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE amount = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setDouble(2, amount);
            statement.setInt(3, month);
            statement.setInt(4, year);
            statement.setDouble(5, amount);

            statement.executeUpdate();

            System.out.println("Budget saved successfully!");

        } catch (Exception e) {
            System.out.println("Failed to save budget!");
            e.printStackTrace();
        }
    }

    public static double getBudget(int userId, int month, int year) {

        String sql =
                "SELECT amount FROM budgets " +
                "WHERE user_id = ? AND month = ? AND year = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("amount");
            }

        } catch (Exception e) {
            System.out.println("Failed to get budget!");
            e.printStackTrace();
        }

        return 0.0;
    }
}