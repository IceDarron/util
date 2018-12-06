package druiddatasource;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = DruidUtils.getCoreConnection();
        try {
            connection.getClientInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
