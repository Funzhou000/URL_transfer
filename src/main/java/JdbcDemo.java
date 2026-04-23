import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcDemo {
    // Database connection credentials (replace with your local MySQL password)
    private static final String DB_URL = "jdbc:mysql://localhost:3307/short_url_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456"; // 修改为你的数据库密码

    public static void main(String[] args) {
        // Step 1: Define the DML (Data Manipulation Language) statement
        // The '?' is a placeholder to prevent SQL Injection
        String sql = "INSERT INTO url_mapping (long_url) VALUES (?)";

        // Step 2: Use try-with-resources to ensure resources are automatically closed
        try (
                // Establish the network connection to MySQL
                Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

                // Create a PreparedStatement, requesting the generated keys (Auto-increment ID)
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            System.out.println("Successfully connected to MySQL database!");

            // Step 3: Set the actual parameter for the placeholder '?'
            String myLongUrl = "https://www.google.com/search?q=ielts+writing+task+2+vocabulary";
            pstmt.setString(1, myLongUrl);

            // Step 4: Execute the update (INSERT, UPDATE, DELETE)
            // executeUpdate() returns the number of affected rows
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Rows inserted: " + affectedRows);

            // Step 5: Retrieve the generated Auto-increment ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    System.out.println("Success! The generated ID is: " + id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}