public class BudgetSummaryTest {

    public static void main(String[] args) {

        int userId = 2;
        int month = 10;
        int year = 2026;

        double budget = BudgetDAO.getBudget(userId, month, year);
        double spent = BudgetSummaryDAO.getMonthlySpent(userId, month, year);
        double remaining = BudgetSummaryDAO.getRemainingBudget(userId, month, year);

        System.out.printf("Monthly Budget: Rs. %.2f%n", budget);
        System.out.printf("Total Spent:    Rs. %.2f%n", spent);
        System.out.printf("Remaining:      Rs. %.2f%n", remaining);
    }
}