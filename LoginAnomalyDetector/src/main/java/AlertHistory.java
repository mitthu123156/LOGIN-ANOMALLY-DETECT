import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AlertHistory {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Security Alerts");

        frame.setSize(850, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        String[] columns = {
                "Alert ID",
                "Username",
                "Risk Score",
                "Risk Level",
                "Alert Message",
                "Alert Time"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);

        table.setRowHeight(28);
        table.setFont(
                new Font("Arial", Font.PLAIN, 13)
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        loadAlerts(model);

        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private static void loadAlerts(
            DefaultTableModel model) {

        String url =
                "jdbc:mysql://localhost:3306/login_anomaly_detector";

        String username = "root";

        String password = "Pupu1901@sdkv";

        String query = """
                SELECT alert_id,
                       username,
                       risk_score,
                       risk_level,
                       alert_message,
                       alert_time
                FROM Alerts
                ORDER BY alert_id DESC
                """;

        try {

            Connection connection =
                    DriverManager.getConnection(
                            url,
                            username,
                            password
                    );

            Statement statement =
                    connection.createStatement();

            ResultSet result =
                    statement.executeQuery(query);

            while (result.next()) {

                model.addRow(new Object[]{
                        result.getInt("alert_id"),
                        result.getString("username"),
                        result.getInt("risk_score"),
                        result.getString("risk_level"),
                        result.getString("alert_message"),
                        result.getTimestamp("alert_time")
                });
            }

            result.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Failed to load alerts:\n"
                            + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}