import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

public class ExpenseDAO {

    // Add a new expense
    public static void addExpense(
            int userId,
            int categoryId,
            double amount,
            String description,
            String expenseDate) {

        String sql = "INSERT INTO expenses "
                   + "(user_id, category_id, amount, description, expense_date) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, categoryId);
            statement.setDouble(3, amount);
            statement.setString(4, description);
            statement.setDate(5, Date.valueOf(expenseDate));

            statement.executeUpdate();

            System.out.println("Expense added successfully!");

        } catch (Exception e) {
            System.out.println("Failed to add expense!");
            e.printStackTrace();
        }
    }

    // Update an existing expense
    public static void updateExpense(
            int expenseId,
            int userId,
            int categoryId,
            double amount,
            String description,
            String expenseDate) {

        String sql = "UPDATE expenses SET "
                   + "category_id = ?, "
                   + "amount = ?, "
                   + "description = ?, "
                   + "expense_date = ? "
                   + "WHERE id = ? AND user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, categoryId);
            statement.setDouble(2, amount);
            statement.setString(3, description);
            statement.setDate(4, Date.valueOf(expenseDate));
            statement.setInt(5, expenseId);
            statement.setInt(6, userId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Expense updated successfully!");
            } else {
                System.out.println(
                    "Expense not found or does not belong to this user."
                );
            }

        } catch (Exception e) {
            System.out.println("Failed to update expense!");
            e.printStackTrace();
        }
    }

    // Delete an expense
    public static void deleteExpense(int expenseId, int userId) {

        String sql = "DELETE FROM expenses WHERE id = ? AND user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, expenseId);
            statement.setInt(2, userId);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Expense deleted successfully!");
            } else {
                System.out.println(
                    "Expense not found or does not belong to this user."
                );
            }

        } catch (Exception e) {
            System.out.println("Failed to delete expense!");
            e.printStackTrace();
        }
    }
}