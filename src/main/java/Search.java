import java.sql.*;

public class Search {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/short_url_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456"; // 修改为你的数据库密码
    private static Connection conn = null;
    private static PreparedStatement pstmt = null;
    private static String sql = "select * from url_mapping where short_url = ?;";

    public Search(String shortURL) {
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, shortURL);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // 通过字段名或索引获取数据
                int id = rs.getInt("id");
                String shortUrl = rs.getString("short_url");
                String longUrl = rs.getString("long_url");
                String date = rs.getString("created_at");
                System.out.println("ID: " + id + ", Short: " + shortUrl + ", Long: " + longUrl+ ", Date: " + date);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
