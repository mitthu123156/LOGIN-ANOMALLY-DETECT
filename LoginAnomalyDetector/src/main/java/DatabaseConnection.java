import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/login_anomaly_detector";
        String username = "root";
        String password = "Pupu1901@sdkv";

        try {

            Connection connection =
                    DriverManager.getConnection(url, username, password);

            System.out.println("Database connected successfully!");

            String query = "SELECT * FROM Users";

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(query);

            System.out.println();
            System.out.println("----- Users -----");

            while (result.next()) {

                int userId = result.getInt("user_id");
                String userName = result.getString("username");
                int startHour = result.getInt("normal_start_hour");
                int endHour = result.getInt("normal_end_hour");

                System.out.println("User ID: " + userId);
                System.out.println("Username: " + userName);
                System.out.println("Normal Login Time: "
                        + startHour + ":00 - " + endHour + ":00");
                System.out.println();
            }

            result.close();
            statement.close();
            connection.close();

        } catch (Exception e) {

            System.out.println("Database operation failed!");
            e.printStackTrace();
        }
    }
}