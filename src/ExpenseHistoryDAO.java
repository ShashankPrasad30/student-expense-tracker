import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExpenseHistoryDAO {

    public static void showExpenses(int userId) {

        String sql = "SELECT e.id, c.name AS category, e.amount, " +
                     "e.description, e.expense_date " +
                     "FROM expenses e " +
                     "JOIN categories c ON e.category_id = c.id " +
                     "WHERE e.user_id = ? " +
                     "ORDER BY e.expense_date DESC, e.id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nExpense History");
            System.out.println("-----------------------------------------------");

            while (resultSet.next()) {

                System.out.println(
                    "ID: " + resultSet.getInt("id") +
                    " | Category: " + resultSet.getString("category") +
                    " | Amount: Rs. " + resultSet.getDouble("amount") +
                    " | Description: " + resultSet.getString("description") +
                    " | Date: " + resultSet.getDate("expense_date")
                );
            }

        } catch (Exception e) {
            System.out.println("Failed to fetch expenses!");
            e.printStackTrace();
        }
    }
}