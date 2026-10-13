import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class LoginDatabase {

    public static void saveLogin(LoginEvent event) {

        String url = "jdbc:mysql://localhost:3306/login_anomaly_detector";
        String username = "root";
        String password = "Pupu1901@sdkv";

        String query = """
                INSERT INTO Login_History
                (username, ip_address, device, failed_attempts,
                 login_hour, risk_score, risk_level)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try {

            Connection connection =
                    DriverManager.getConnection(url, username, password);

            PreparedStatement statement =
                    connection.prepareStatement(query);

            statement.setString(1, event.username);
            statement.setString(2, event.ipAddress);
            statement.setString(3, event.device);
            statement.setInt(4, event.failedAttempts);
            statement.setInt(5, event.loginHour);
            statement.setInt(6, event.riskScore);
            statement.setString(7, event.riskLevel);

            statement.executeUpdate();

            System.out.println("Login event saved to database!");

            statement.close();
            connection.close();

        } catch (Exception e) {

            System.out.println("Failed to save login event!");
            e.printStackTrace();
        }
    }
}