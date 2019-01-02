package druiddatasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Main {
    public static void main(String[] args) {
        Connection connection = DruidUtils.getCoreConnection();

        PreparedStatement statement = null;

        // 测试连接是否正常
        try {
            System.out.println(connection.getMetaData());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 测试insert
        int[] counts = null;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(
                    " insert into C_RCA_CONS_STATUS_COMPARE(" +
                            "RECORD_ID," +
                            "CONS_NO," +
                            "ORG_NO," +
                            "CREATE_TIME," +
                            "ORACLE_STATUS," +
                            "REDIS_STATUS," +
                            "DIFFERENT) " +
                            "values (?,?,?,?,?,?,?) ");

            int i = 1;
            statement.setLong(i++,0);
            statement.setString(i++, "0253287379");
            statement.setString(i++, "3645119");
            statement.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
            statement.setString(i++, "1");
            statement.setString(i++, "1");
            statement.setString(i++, "1");

            statement.addBatch();
            counts = statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            try {
                e.printStackTrace();
                connection.rollback();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        } finally {
            DruidUtils.release(connection, statement);
        }
        System.out.println(counts);
    }
}
