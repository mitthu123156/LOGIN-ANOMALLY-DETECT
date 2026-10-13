public class LoginEvent {

    String username;
    String ipAddress;
    String device;
    int failedAttempts;
    int loginHour;
    int riskScore;
    String riskLevel;
    String riskReasons;

    public LoginEvent(String username, String ipAddress, String device,
                      int failedAttempts, int loginHour) {

        this.username = username;
        this.ipAddress = ipAddress;
        this.device = device;
        this.failedAttempts = failedAttempts;
        this.loginHour = loginHour;
    }
}