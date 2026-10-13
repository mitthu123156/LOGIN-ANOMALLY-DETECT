import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AlertDatabase {

    public static void saveAlert(LoginEvent event) {

        // Database connection details
        String url = "jdbc:mysql://localhost:3306/login_anomaly_detector";
        String username = "root";
        String password = "Pupu1901@sdkv";

        String query = """
                INSERT INTO Alerts
                (username, risk_score, risk_level, alert_message)
                VALUES (?, ?, ?, ?)
                """;

        try {

            Connection connection =
                    DriverManager.getConnection(url, username, password);

            PreparedStatement statement =
                    connection.prepareStatement(query);

            statement.setString(1, event.username);
            statement.setInt(2, event.riskScore);
            statement.setString(3, event.riskLevel);
            statement.setString(4, "Suspicious login detected!");

            statement.executeUpdate();

            System.out.println("Security alert saved to database!");

            statement.close();
            connection.close();

        } catch (Exception e) {

            System.out.println("Failed to save security alert!");
            e.printStackTrace();
        }
    }
}