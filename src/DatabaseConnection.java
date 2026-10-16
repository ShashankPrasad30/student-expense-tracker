
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {

        Properties properties = new Properties();

        // Load local database credentials
        try (FileInputStream input =
                     new FileInputStream("config.properties")) {

            properties.load(input);

        } catch (IOException e) {
            throw new SQLException(
                "Cannot load config.properties. " +
                "Make sure it exists in the project root.",
                e
            );
        }

        // Use the test URL only when explicitly supplied at runtime.
        // Otherwise, use the normal application database URL.
        String url = System.getProperty(
            "db.url",
            properties.getProperty("db.url")
        );

        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (url == null || username == null || password == null
                || url.isBlank()
                || username.isBlank()
                || password.isBlank()) {

            throw new SQLException(
                "Database settings are missing or incomplete."
            );
        }

        return DriverManager.getConnection(url, username, password);
    }
}
