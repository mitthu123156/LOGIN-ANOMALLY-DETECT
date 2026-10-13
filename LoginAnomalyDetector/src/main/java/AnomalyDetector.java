import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AnomalyDetector {

    public static void analyze(LoginEvent event) {

        int riskScore = 0;

        StringBuilder reasons =
                new StringBuilder();

        // =====================================================
        // GET USER'S NORMAL LOGIN TIME FROM DATABASE
        // =====================================================

        int normalStartHour = 9;
        int normalEndHour = 18;

        String url =
                "jdbc:mysql://localhost:3306/login_anomaly_detector";

        String username = "root";

        String password =
                "Pupu1901@sdkv";

        try {

            Connection connection =
                    DriverManager.getConnection(
                            url,
                            username,
                            password
                    );

            // -------------------------------------------------
            // Find user's normal login hours
            // -------------------------------------------------

            String userQuery = """
                    SELECT normal_start_hour,
                           normal_end_hour
                    FROM Users
                    WHERE username = ?
                    """;

            PreparedStatement userStatement =
                    connection.prepareStatement(userQuery);

            userStatement.setString(
                    1,
                    event.username
            );

            ResultSet userResult =
                    userStatement.executeQuery();

            if (userResult.next()) {

                normalStartHour =
                        userResult.getInt(
                                "normal_start_hour"
                        );

                normalEndHour =
                        userResult.getInt(
                                "normal_end_hour"
                        );
            }

            userResult.close();
            userStatement.close();

            // =================================================
            // RULE 1 — FAILED ATTEMPTS
            // =================================================

            if (event.failedAttempts >= 3) {

                riskScore += 30;

                reasons.append(
                        "Multiple failed attempts (+30)\n"
                );
            }

            // =================================================
            // RULE 2 — UNUSUAL LOGIN TIME
            // =================================================

            if (event.loginHour < normalStartHour ||
                    event.loginHour > normalEndHour) {

                riskScore += 20;

                reasons.append(
                        "Unusual login time (+20)\n"
                );
            }

            // =================================================
            // RULE 3 — NEW IP ADDRESS
            // =================================================

            boolean knownIP =
                    isKnownIP(
                            connection,
                            event.username,
                            event.ipAddress
                    );

            if (!knownIP) {

                riskScore += 15;

                reasons.append(
                        "New IP address (+15)\n"
                );
            }

            // =================================================
            // RULE 4 — NEW DEVICE
            // =================================================

            boolean knownDevice =
                    isKnownDevice(
                            connection,
                            event.username,
                            event.device
                    );

            if (!knownDevice) {

                riskScore += 25;

                reasons.append(
                        "Unknown device (+25)\n"
                );
            }

            // =================================================
            // STORE RESULTS
            // =================================================

            event.riskScore =
                    riskScore;

            event.riskReasons =
                    reasons.toString();

            // =================================================
            // DETERMINE RISK LEVEL
            // =================================================

            if (riskScore >= 61) {

                event.riskLevel = "HIGH";

            } else if (riskScore >= 31) {

                event.riskLevel = "MEDIUM";

            } else {

                event.riskLevel = "LOW";
            }

            connection.close();

        } catch (Exception e) {

            System.out.println(
                    "Anomaly detection database error:"
            );

            e.printStackTrace();

            // Safe fallback
            event.riskScore = riskScore;

            event.riskReasons =
                    reasons.toString();

            if (riskScore >= 61) {

                event.riskLevel = "HIGH";

            } else if (riskScore >= 31) {

                event.riskLevel = "MEDIUM";

            } else {

                event.riskLevel = "LOW";
            }
        }
    }

    // =========================================================
    // CHECK WHETHER IP WAS USED BEFORE
    // =========================================================

    private static boolean isKnownIP(
            Connection connection,
            String username,
            String ipAddress
    ) throws Exception {

        String query = """
                SELECT COUNT(*)
                FROM Login_History
                WHERE username = ?
                AND ip_address = ?
                """;

        PreparedStatement statement =
                connection.prepareStatement(query);

        statement.setString(
                1,
                username
        );

        statement.setString(
                2,
                ipAddress
        );

        ResultSet result =
                statement.executeQuery();

        boolean known = false;

        if (result.next()) {

            known =
                    result.getInt(1) > 0;
        }

        result.close();
        statement.close();

        return known;
    }

    // =========================================================
    // CHECK WHETHER DEVICE WAS USED BEFORE
    // =========================================================

    private static boolean isKnownDevice(
            Connection connection,
            String username,
            String device
    ) throws Exception {

        String query = """
                SELECT COUNT(*)
                FROM Login_History
                WHERE username = ?
                AND device = ?
                """;

        PreparedStatement statement =
                connection.prepareStatement(query);

        statement.setString(
                1,
                username
        );

        statement.setString(
                2,
                device
        );

        ResultSet result =
                statement.executeQuery();

        boolean known = false;

        if (result.next()) {

            known =
                    result.getInt(1) > 0;
        }

        result.close();
        statement.close();

        return known;
    }
}