package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class _01_Test {

    static Connection conn = null;
    static PreparedStatement ptmt = null;
    static ResultSet rs = null;


    public static void main(String[] args) {
        conn = JdbcUtil.testgetconnection();
//		List list = new ArrayList();
//		List result = new ArrayList();
//		String sql = "select * from ? where to_char(cjsj,'yyyymmdd') > '?' or to_char(zhxgsj,'yyyymmdd') > '?'";
//        Map map = new HashMap();
//		for (int i = 0; i < list.size(); i++) {
//			try {
//				ptmt = conn.prepareStatement(sql);
//				rs = ptmt.executeQuery();
//				ptmt.setString(1, (String) list.get(i));
//				while (rs.next()) {
//					
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} finally {
//				new JdbcUtil().close(conn, ptmt, rs);
//			}
//		}
        JdbcUtil.close(conn, ptmt, rs);
    }
}
