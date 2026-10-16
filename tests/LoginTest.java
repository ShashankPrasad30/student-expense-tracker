public class LoginTest {

    public static void main(String[] args) {

        int userId = LoginDAO.loginUser(
            "teststudent@example.com",
            "test123"
        );

        if (userId != -1) {
            System.out.println("Logged-in User ID: " + userId);
        }
    }
}