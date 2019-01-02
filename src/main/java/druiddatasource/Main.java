package druiddatasource;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = DruidUtils.getCoreConnection();
        try {
            System.out.println(connection.getMetaData());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
