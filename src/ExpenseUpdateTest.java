public class ExpenseUpdateTest {

    public static void main(String[] args) {

        ExpenseDAO.updateExpense(
            3,
            2,
            1,
            300.00,
            "Lunch and snacks",
            "2026-10-05"
        );
    }
}