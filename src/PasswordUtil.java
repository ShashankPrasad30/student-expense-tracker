
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Locale;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final int ITERATIONS = 210_000;
    private static final int SALT_LENGTH = 16;
    private static final int KEY_LENGTH = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    // Create a salted PBKDF2 hash for a new password
    public static String hashPassword(String password) {

        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);

        byte[] hash = deriveKey(
            password.toCharArray(),
            salt,
            ITERATIONS
        );

        return "pbkdf2$"
                + ITERATIONS + "$"
                + HexFormat.of().formatHex(salt) + "$"
                + HexFormat.of().formatHex(hash);
    }

    // Verify a password against its stored hash
    public static boolean verifyPassword(
            String password, String storedHash) {

        if (storedHash == null) {
            return false;
        }

        // Verify the newer PBKDF2 format
        if (storedHash.startsWith("pbkdf2$")) {

            String[] parts = storedHash.split("\\$", -1);

            if (parts.length != 4) {
                return false;
            }

            try {
                int iterations = Integer.parseInt(parts[1]);

                if (iterations < 100_000 || iterations > 1_000_000) {
                    return false;
                }

                byte[] salt = HexFormat.of().parseHex(parts[2]);
                byte[] expectedHash = HexFormat.of().parseHex(parts[3]);

                if (salt.length != SALT_LENGTH
                        || expectedHash.length != KEY_LENGTH / 8) {
                    return false;
                }

                byte[] actualHash = deriveKey(
                    password.toCharArray(),
                    salt,
                    iterations
                );

                return MessageDigest.isEqual(expectedHash, actualHash);

            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        // Support older SHA-256 hashes so existing accounts still work
        if (isLegacyHash(storedHash)) {
            String oldHash = legacySHA256(password);

            return MessageDigest.isEqual(
                storedHash.toLowerCase(Locale.ROOT)
                    .getBytes(StandardCharsets.UTF_8),
                oldHash.getBytes(StandardCharsets.UTF_8)
            );
        }

        return false;
    }

    // Identify hashes created by the old SHA-256 implementation
    public static boolean isLegacyHash(String storedHash) {

        return storedHash != null
                && storedHash.matches("(?i)[0-9a-f]{64}");
    }

    // Derive a password hash using PBKDF2-HMAC-SHA256
    private static byte[] deriveKey(
            char[] password,
            byte[] salt,
            int iterations) {

        PBEKeySpec spec = new PBEKeySpec(
            password,
            salt,
            iterations,
            KEY_LENGTH
        );

        try {
            SecretKeyFactory factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            return factory.generateSecret(spec).getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                "Password hashing failed.", e
            );

        } finally {
            spec.clearPassword();
            Arrays.fill(password, '\0');
        }
    }

    // Legacy SHA-256 support for existing accounts
    private static String legacySHA256(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(
                "SHA-256 is not available.", e
            );
        }
    }
}