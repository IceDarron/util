package sql;

import java.sql.*;

public class JdbcUtil {
    static Connection conn = null;
    static PreparedStatement ptmt = null;
    static ResultSet rs = null;

    public static Connection testgetconnection() {
        // 1、加载数据库
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:orcl", "scott", "tiger");
            System.out.println("连接成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭资源
    public static void close(Connection conn, PreparedStatement ptmt,
                             ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (ptmt != null) {
            try {
                ptmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
