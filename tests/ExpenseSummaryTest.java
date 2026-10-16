public class ExpenseSummaryTest {

    public static void main(String[] args) {

        double total = ExpenseSummaryDAO.getTotalSpent(2);

        System.out.printf("Total Spent: Rs. %.2f%n", total);
    }
}