import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategorySummaryDAO {

    public static void showCategoryWiseSpending(int userId) {

        String sql =
                "SELECT c.name AS category, " +
                "COALESCE(SUM(e.amount), 0) AS total " +
                "FROM categories c " +
                "LEFT JOIN expenses e " +
                "ON c.id = e.category_id " +
                "AND e.user_id = ? " +
                "GROUP BY c.id, c.name " +
                "ORDER BY total DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("\nCategory-wise Spending");
            System.out.println("--------------------------------");

            while (resultSet.next()) {

                String category = resultSet.getString("category");
                double total = resultSet.getDouble("total");

                System.out.printf(
                        "%-15s : Rs. %.2f%n",
                        category,
                        total
                );
            }

        } catch (Exception e) {
            System.out.println("Failed to calculate category-wise spending!");
            e.printStackTrace();
        }
    }
}