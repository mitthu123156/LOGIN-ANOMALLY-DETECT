import java.util.Scanner;

public class LoginSimulator {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("      LOGIN ANOMALY DETECTOR");
        System.out.println("=================================");

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter IP address: ");
        String ipAddress = input.nextLine();

        System.out.print("Enter device name: ");
        String device = input.nextLine();

        System.out.print("Enter number of failed attempts: ");
        int failedAttempts = input.nextInt();

        System.out.print("Enter login hour (0-23): ");
        int loginHour = input.nextInt();

        // Create login event
        LoginEvent event = new LoginEvent(
                username,
                ipAddress,
                device,
                failedAttempts,
                loginHour
        );

        // Analyze the login
        AnomalyDetector.analyze(event);

        // Save to database
        LoginDatabase.saveLogin(event);

        // Display result
        System.out.println();
        System.out.println("---------- LOGIN ANALYSIS ----------");

        System.out.println("Username: " + event.username);
        System.out.println("IP Address: " + event.ipAddress);
        System.out.println("Device: " + event.device);
        System.out.println("Failed Attempts: " + event.failedAttempts);
        System.out.println("Login Hour: " + event.loginHour);

        System.out.println();
        System.out.println("Risk Score: " + event.riskScore + "/100");
        System.out.println("Risk Level: " + event.riskLevel);
        System.out.println();
        System.out.println("Detected Issues:");

        if (event.riskReasons.isEmpty()) {
            System.out.println("No suspicious activity detected.");
        } else {
            System.out.print(event.riskReasons);
        }

        if (event.riskLevel.equals("HIGH")) {
            System.out.println();
            System.out.println("!!! SECURITY ALERT !!!");
            System.out.println("Suspicious login detected!");

            AlertDatabase.saveAlert(event);
        }

        input.close();
    }
}