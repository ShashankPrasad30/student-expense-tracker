
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n================================");
            System.out.println("     STUDENT EXPENSE TRACKER");
            System.out.println("================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            String choice = readNonEmpty("Enter your choice: ");

            switch (choice) {
                case "1":
                    register();
                    break;

                case "2":
                    login();
                    break;

                case "3":
                    System.out.println(
                        "Thank you for using Student Expense Tracker!"
                    );
                    scanner.close();
                    return;

                default:
                    System.out.println(
                        "Invalid choice. Please enter 1, 2, or 3."
                    );
            }
        }
    }

    // Register a new user
    private static void register() {

        System.out.println("\n--------- REGISTER ---------");

        String name = readNonEmpty("Enter your name: ");
        String email = readEmail();
        String password = readNewPassword();

        UserDAO.registerUser(name, email, password);
    }

    // Login an existing user
    private static void login() {

        System.out.println("\n----------- LOGIN -----------");

        String email = readEmail();
        String password = readPassword("Enter your password: ");

        int userId = LoginDAO.loginUser(email, password);

        if (userId != -1) {
            dashboard(userId);
        }
    }

    // Dashboard after successful login
    private static void dashboard(int userId) {

        while (true) {

            System.out.println("\n================================");
            System.out.println("           DASHBOARD");
            System.out.println("================================");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Update Expense");
            System.out.println("4. Delete Expense");
            System.out.println("5. Total Spending");
            System.out.println("6. Category-wise Spending");
            System.out.println("7. Set Monthly Budget");
            System.out.println("8. View Monthly Budget");
            System.out.println("9. Logout");

            String choice = readNonEmpty("Enter your choice: ");

            switch (choice) {
                case "1":
                    addExpense(userId);
                    break;

                case "2":
                    viewExpenses(userId);
                    break;

                case "3":
                    updateExpense(userId);
                    break;

                case "4":
                    deleteExpense(userId);
                    break;

                case "5":
                    showTotalSpending(userId);
                    break;

                case "6":
                    showCategorySpending(userId);
                    break;

                case "7":
                    setBudget(userId);
                    break;

                case "8":
                    viewBudget(userId);
                    break;

                case "9":
                    System.out.println("Logged out successfully.");
                    return;

                default:
                    System.out.println(
                        "Invalid choice. Please enter a number from 1 to 9."
                    );
            }
        }
    }

    // Display available expense categories
    private static void showCategories() {

        System.out.println("\nCategories:");
        System.out.println("1. Food");
        System.out.println("2. Travel");
        System.out.println("3. Education");
        System.out.println("4. Shopping");
        System.out.println("5. Entertainment");
        System.out.println("6. Bills");
        System.out.println("7. Health");
        System.out.println("8. Other");
    }

    // Add an expense
    private static void addExpense(int userId) {

        System.out.println("\n--------- ADD EXPENSE ---------");

        showCategories();

        int categoryId = readIntInRange(
            "Enter category ID: ", 1, 8
        );

        double amount = readPositiveAmount("Enter amount: ");
        String description = readNonEmpty("Enter description: ");
        String date = readDate("Enter date (YYYY-MM-DD): ");

        ExpenseDAO.addExpense(
            userId,
            categoryId,
            amount,
            description,
            date
        );
    }

    // View expense history
    private static void viewExpenses(int userId) {

        ExpenseHistoryDAO.showExpenses(userId);
    }

    // Update an existing expense
    private static void updateExpense(int userId) {

        System.out.println("\n--------- UPDATE EXPENSE ---------");

        int expenseId = readIntInRange(
            "Enter expense ID: ", 1, Integer.MAX_VALUE
        );

        showCategories();

        int categoryId = readIntInRange(
            "Enter new category ID: ", 1, 8
        );

        double amount = readPositiveAmount("Enter new amount: ");
        String description = readNonEmpty("Enter new description: ");
        String date = readDate("Enter new date (YYYY-MM-DD): ");

        ExpenseDAO.updateExpense(
            expenseId,
            userId,
            categoryId,
            amount,
            description,
            date
        );
    }

    // Delete an expense after confirmation
    private static void deleteExpense(int userId) {

        System.out.println("\n--------- DELETE EXPENSE ---------");

        int expenseId = readIntInRange(
            "Enter expense ID: ", 1, Integer.MAX_VALUE
        );

        String confirmation = readNonEmpty(
            "Are you sure you want to delete this expense? (y/n): "
        );

        if (confirmation.equalsIgnoreCase("y")) {
            ExpenseDAO.deleteExpense(expenseId, userId);
        } else if (confirmation.equalsIgnoreCase("n")) {
            System.out.println("Deletion cancelled.");
        } else {
            System.out.println(
                "Invalid confirmation. Deletion cancelled."
            );
        }
    }

    // Calculate and display total spending
    private static void showTotalSpending(int userId) {

        double total = ExpenseSummaryDAO.getTotalSpent(userId);

        System.out.printf("Total Spent: Rs. %.2f%n", total);
    }

    // Display category-wise spending
    private static void showCategorySpending(int userId) {

        CategorySummaryDAO.showCategoryWiseSpending(userId);
    }

    // Set or update a monthly budget
    private static void setBudget(int userId) {

        System.out.println("\n--------- SET MONTHLY BUDGET ---------");

        double amount = readPositiveAmount("Enter budget amount: ");

        int month = readIntInRange(
            "Enter month (1-12): ", 1, 12
        );

        int year = readIntInRange(
            "Enter year (1901-2155): ", 1901, 2155
        );

        BudgetDAO.setBudget(userId, amount, month, year);
    }

    // View monthly budget and remaining amount
    private static void viewBudget(int userId) {

        System.out.println("\n--------- MONTHLY BUDGET ---------");

        int month = readIntInRange(
            "Enter month (1-12): ", 1, 12
        );

        int year = readIntInRange(
            "Enter year (1901-2155): ", 1901, 2155
        );

        double budget = BudgetDAO.getBudget(userId, month, year);

        double spent = BudgetSummaryDAO.getMonthlySpent(
            userId, month, year
        );

        System.out.printf("Monthly Budget: Rs. %.2f%n", budget);
        System.out.printf("Total Spent:    Rs. %.2f%n", spent);

        if (budget <= 0) {
            System.out.println("No budget has been set for this month.");
        } else {
            double remaining = budget - spent;
            System.out.printf("Remaining:      Rs. %.2f%n", remaining);
        }
    }

    // Read non-empty text
    private static String readNonEmpty(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    // Read and validate an email address
    private static String readEmail() {

        while (true) {
            String email = readNonEmpty("Enter your email: ");

            if (email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                return email;
            }

            System.out.println(
                "Invalid email format. Please enter a valid email."
            );
        }
    }

    // Read a password, preserving spaces within it
    private static String readPassword(String prompt) {

        while (true) {
            System.out.print(prompt);
            String password = scanner.nextLine();

            if (!password.trim().isEmpty()) {
                return password;
            }

            System.out.println("Password cannot be empty.");
        }
    }

    // Require a new password of at least 8 characters
    // and confirm that both password entries match
    private static String readNewPassword() {

        while (true) {

            String password = readPassword(
                "Enter password (at least 8 characters): "
            );

            if (password.length() < 8) {
                System.out.println(
                    "Password must contain at least 8 characters."
                );
                continue;
            }

            String confirmPassword = readPassword(
                "Confirm your password: "
            );

            if (password.equals(confirmPassword)) {
                return password;
            }

            System.out.println(
                "Passwords do not match. Please try again."
            );
        }
    }

    // Read a whole number within an allowed range
    private static int readIntInRange(
            String prompt, int min, int max) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);

                if (value >= min && value <= max) {
                    return value;
                }

                System.out.println(
                    "Please enter a number between "
                    + min + " and " + max + "."
                );

            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid input. Please enter a whole number."
                );
            }
        }
    }

    // Read a positive, finite amount
    private static double readPositiveAmount(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                double amount = Double.parseDouble(input);

                if (Double.isFinite(amount) && amount > 0) {
                    return amount;
                }

                System.out.println(
                    "Amount must be a valid number greater than zero."
                );

            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid amount. Please enter a number, such as 250.50."
                );
            }
        }
    }

    // Read a valid date in YYYY-MM-DD format
    private static String readDate(String prompt) {

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    throw new DateTimeParseException(
                        "Expected YYYY-MM-DD", input, 0
                    );
                }

                LocalDate date = LocalDate.parse(input);

                if (date.getYear() < 1000) {
                    System.out.println(
                        "Please enter a date from year 1000 onwards."
                    );
                    continue;
                }

                return date.toString();

            } catch (DateTimeParseException e) {
                System.out.println(
                    "Invalid date. Use YYYY-MM-DD, e.g. 2026-10-09."
                );
            }
        }
    }
}