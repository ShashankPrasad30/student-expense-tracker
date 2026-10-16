
public class PasswordUtilTest {

    public static void main(String[] args) {

        String password = "ExpenseDemo26!";

        // Test 1: Generate a password hash
        String hash1 = PasswordUtil.hashPassword(password);

        check(
            hash1.startsWith("pbkdf2$"),
            "Password should use PBKDF2 hashing."
        );
        System.out.println("Test 1 passed: Password hashing.");

        // Test 2: Verify the correct password
        check(
            PasswordUtil.verifyPassword(password, hash1),
            "Correct password should be accepted."
        );
        System.out.println("Test 2 passed: Correct password accepted.");

        // Test 3: Reject an incorrect password
        check(
            !PasswordUtil.verifyPassword("WrongPassword123!", hash1),
            "Incorrect password should be rejected."
        );
        System.out.println("Test 3 passed: Incorrect password rejected.");

        // Test 4: Confirm that separate hashes use different salts
        String hash2 = PasswordUtil.hashPassword(password);

        check(
            !hash1.equals(hash2),
            "Separate hashes should differ because of random salts."
        );
        System.out.println("Test 4 passed: Random salts.");

        // Test 5: Reject an invalid stored hash
        check(
            !PasswordUtil.verifyPassword(password, "invalid-hash"),
            "Malformed hash should be rejected."
        );
        System.out.println("Test 5 passed: Invalid hash rejected.");

        System.out.println("\nAll password tests passed!");
    }

    private static void check(boolean condition, String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
