import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExpenseSummaryDAO {

    public static double getTotalSpent(int userId) {

        String sql = "SELECT COALESCE(SUM(amount), 0) AS total " +
                     "FROM expenses WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

        } catch (Exception e) {
            System.out.println("Failed to calculate total spending!");
            e.printStackTrace();
        }

        return 0.0;
    }
}