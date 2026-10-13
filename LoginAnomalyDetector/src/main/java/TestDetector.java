public class TestDetector {

    public static void main(String[] args) {

        LoginEvent event = new LoginEvent(
                "kartavya01",
                "45.23.67.89",
                "Unknown-Phone",
                5,
                2
        );

        AnomalyDetector.analyze(event);
        LoginDatabase.saveLogin(event);

        System.out.println("----- Login Analysis -----");

        System.out.println("Username: " + event.username);
        System.out.println("IP Address: " + event.ipAddress);
        System.out.println("Device: " + event.device);
        System.out.println("Failed Attempts: " + event.failedAttempts);
        System.out.println("Login Hour: " + event.loginHour);

        System.out.println("Risk Score: " + event.riskScore + "/100");
        System.out.println("Risk Level: " + event.riskLevel);
    }
}