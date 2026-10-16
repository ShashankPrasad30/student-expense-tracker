import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BudgetSummaryDAO {

    public static double getMonthlySpent(int userId, int month, int year) {

        String sql =
                "SELECT COALESCE(SUM(amount), 0) AS total " +
                "FROM expenses " +
                "WHERE user_id = ? " +
                "AND MONTH(expense_date) = ? " +
                "AND YEAR(expense_date) = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, month);
            statement.setInt(3, year);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println("Failed to calculate monthly spending!");
            e.printStackTrace();
        }

        return 0.0;
    }

    public static double getRemainingBudget(int userId, int month, int year) {

        double budget = BudgetDAO.getBudget(userId, month, year);
        double spent = getMonthlySpent(userId, month, year);

        return budget - spent;
    }
}