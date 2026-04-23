package Util;

public final class Util_WriteToDB {
//    一个设计良好的工具类通常具备以下五个技术特征：
//
//    Final Class（最终类）：类应该声明为 final，以防止被其他类继承。因为工具类只有静态方法，继承它是没有意义的。
//
//    Private Constructor（私有构造函数）：必须显式提供一个私有的构造函数，防止外部通过 new 关键字将其实例化。
//
//    Static Methods（静态方法）：所有的方法都应该是 static（静态的）。调用时直接通过 ClassName.methodName() 访问，无需创建对象。
//
//    Stateless（无状态）：工具类通常不包含成员变量。如果包含，通常也只是 static final 类型的常量。它不应该保存任何会影响后续调用的中间状态。
//
//    Side-Effect Free（无副作用）：理想情况下，工具类方法应该是 Pure Functions（纯函数），即相同的输入总是得到相同的输出，且不修改传入的对象。
// Database connection credentials (replace with your local MySQL password)
    private static final String DB_URL = "jdbc:mysql://localhost:3307/short_url_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456"; // 修改为你的数据库密码

    private Util_WriteToDB() {
    }
    public static boolean writeData(String longURL, String shortURL) {
        // Implementation for writing data to DB
        return true;
    }
}
