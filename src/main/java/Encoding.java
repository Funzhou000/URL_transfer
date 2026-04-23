import java.io.*;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Base64;

public class Encoding {
    static String txtSource ="/Users/funzhou/Documents/javacode/TransferURL/src/main/resources/LongURL.txt";
    private static final String DB_URL = "jdbc:mysql://localhost:3307/short_url_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456"; // 修改为你的数据库密码
    private static Connection conn = null;
    private static PreparedStatement pstmt = null;
    private static String sql = "INSERT INTO url_mapping (short_url,long_url) VALUES (?,?)";
    private static int status = 1;//defaut0  after encoding and insert turn it =1
    public Encoding() {
        if (status == 0) {
            try (BufferedReader bfr = new BufferedReader(new FileReader(txtSource))) {
                String line;
                    getConnection();
                    int i = 0;

                while((line =bfr.readLine())!=null) {
                    String shortURL = generateShortURL(line);//LongURL
                    if (i % 500 == 0) {
                        pstmt.executeBatch();
                        conn.commit(); // 提交事务
                        pstmt.clearBatch(); // 清空已执行的缓存
                    }
                    i++;
                    writeToDb(shortURL,line);
                    System.out.println("Processing line " + i + ": " + line+ " -> " + shortURL);
                }
                System.out.println("Done! Total lines processed: " + i);



            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else {
            System.out.println("Data already encoded and inserted. Skipping processing.");
        }
    }

    public static String generateShortURL(String longURL) throws Exception {
        // 1. 获取 MD5 实例
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(longURL.getBytes());

        // 2. 使用 URL-Safe Base64 编码
        // 注意：为了让 URL 更短，我们通常只取 hash 的前 6-8 个字节
        String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);

        // 3. 截取前 8 位作为短码
        return base64Encoded.substring(0, 8);
    }
    public static void getConnection() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        conn.setAutoCommit(false);//turn off the autocommit, commit manual faster than auto commit
        pstmt = conn.prepareStatement(sql);
        //准备好 prestatement
    }
    public static void writeToDb(String shortURL,String longURL) throws Exception {
        pstmt.setString(1, shortURL);
        pstmt.setString(2, longURL);
        pstmt.addBatch();

    }
}
