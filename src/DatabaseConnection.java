
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {

        Properties properties = new Properties();

        // Load the local database configuration
        try (FileInputStream input =
                     new FileInputStream("config.properties")) {

            properties.load(input);

        } catch (IOException e) {
            throw new SQLException(
                "Cannot load config.properties. " +
                "Make sure it is in the project root.",
                e
            );
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        // Check that all required settings are present
        if (url == null || username == null || password == null
                || url.isBlank()
                || username.isBlank()
                || password.isBlank()
                || password.equals("YOUR_ACTUAL_NEW_MYSQL_PASSWORD")) {

            throw new SQLException(
                "Database settings are missing or incomplete."
            );
        }

        return DriverManager.getConnection(url, username, password);
    }
}
