
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigDecimal;
import java.util.UUID;

public class ExpenseCrudIntegrationTest {

    private static final String TEST_DATABASE =
            "student_expense_tracker_test";

    public static void main(String[] args) {

        int userId = -1;
        boolean testDatabaseVerified = false;

        try {
            int categoryId;

            // Confirm that this test is using the test database.
            try (Connection connection =
                         DatabaseConnection.getConnection()) {

                String databaseName = connection.getCatalog();

                if (!TEST_DATABASE.equals(databaseName)) {
                    throw new IllegalStateException(
                        "Safety check failed. Expected database: "
                        + TEST_DATABASE + ", found: " + databaseName
                    );
                }

                testDatabaseVerified = true;

                categoryId = findCategoryId(connection, "Food");

                userId = createTestUser(connection);
            }

            System.out.println("Test database verified.");

            // TEST 1: Add an expense.
            ExpenseDAO.addExpense(
                userId,
                categoryId,
                250.50,
                "CRUD test expense",
                "2026-10-16"
            );

            int expenseId = findExpenseId(
                userId, "CRUD test expense"
            );

            verifyExpense(
                expenseId,
                userId,
                new BigDecimal("250.50"),
                "CRUD test expense"
            );

            System.out.println("PASS: Add expense.");

            // TEST 2: Update the expense.
            ExpenseDAO.updateExpense(
                expenseId,
                userId,
                categoryId,
                300.75,
                "CRUD test updated",
                "2026-10-17"
            );

            verifyExpense(
                expenseId,
                userId,
                new BigDecimal("300.75"),
                "CRUD test updated"
            );

            System.out.println("PASS: Update expense.");

            // TEST 3: Delete the expense.
            ExpenseDAO.deleteExpense(expenseId, userId);

            if (expenseExists(expenseId, userId)) {
                throw new AssertionError(
                    "Expense still exists after deletion."
                );
            }

            System.out.println("PASS: Delete expense.");
            System.out.println(
                "\nAll expense CRUD integration tests passed!"
            );

        } catch (Exception e) {
            System.err.println("\nIntegration test failed:");
            e.printStackTrace();
            throw new RuntimeException("Test failed.", e);

        } finally {
            // Remove only the temporary test user's records.
            if (testDatabaseVerified && userId > 0) {
                cleanupTestUser(userId);
            }
        }
    }

    private static int findCategoryId(
            Connection connection, String categoryName)
            throws SQLException {

        String sql = "SELECT id FROM categories WHERE name = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, categoryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }

        throw new SQLException(
            "Category not found: " + categoryName
        );
    }

    private static int createTestUser(Connection connection)
            throws SQLException {

        String email =
            "crud-test-" + UUID.randomUUID() + "@example.com";

        String sql =
            "INSERT INTO users (name, email, password) "
            + "VALUES (?, ?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(
                         sql, Statement.RETURN_GENERATED_KEYS
                     )) {

            statement.setString(1, "CRUD Test User");
            statement.setString(2, email);
            statement.setString(
                3,
                PasswordUtil.hashPassword("IntegrationTest26!")
            );

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }

        throw new SQLException("Could not create the test user.");
    }

    private static int findExpenseId(
            int userId, String description) throws SQLException {

        String sql =
            "SELECT id FROM expenses "
            + "WHERE user_id = ? AND description = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, description);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }

        throw new SQLException(
            "The inserted test expense was not found."
        );
    }

    private static void verifyExpense(
            int expenseId,
            int userId,
            BigDecimal expectedAmount,
            String expectedDescription) throws SQLException {

        String sql =
            "SELECT amount, description FROM expenses "
            + "WHERE id = ? AND user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, expenseId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    throw new AssertionError(
                        "Expected expense was not found."
                    );
                }

                BigDecimal actualAmount =
                    resultSet.getBigDecimal("amount");

                String actualDescription =
                    resultSet.getString("description");

                if (actualAmount.compareTo(expectedAmount) != 0
                        || !expectedDescription.equals(
                            actualDescription
                        )) {

                    throw new AssertionError(
                        "Expense values do not match expectations."
                    );
                }
            }
        }
    }

    private static boolean expenseExists(
            int expenseId, int userId) throws SQLException {

        String sql =
            "SELECT COUNT(*) FROM expenses "
            + "WHERE id = ? AND user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, expenseId);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next()
                    && resultSet.getInt(1) > 0;
            }
        }
    }

    private static void cleanupTestUser(int userId) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            // Refuse to clean up unless this is the test database.
            if (!TEST_DATABASE.equals(connection.getCatalog())) {
                System.err.println(
                    "Safety check failed: test cleanup skipped."
                );
                return;
            }

            // Remove child records before deleting the user.
            try (PreparedStatement expenses =
                    connection.prepareStatement(
                        "DELETE FROM expenses WHERE user_id = ?"
                    );
                 PreparedStatement budgets =
                    connection.prepareStatement(
                        "DELETE FROM budgets WHERE user_id = ?"
                    );
                 PreparedStatement user =
                    connection.prepareStatement(
                        "DELETE FROM users WHERE id = ?"
                    )) {

                expenses.setInt(1, userId);
                expenses.executeUpdate();

                budgets.setInt(1, userId);
                budgets.executeUpdate();

                user.setInt(1, userId);
                user.executeUpdate();
            }

            System.out.println("Temporary test data cleaned up.");

        } catch (SQLException e) {
            System.err.println(
                "Warning: Could not clean up test data."
            );
            e.printStackTrace();
        }
    }
}
