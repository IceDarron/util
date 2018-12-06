package druiddatasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DruidUtils {
    private static volatile DataSource ds_core;

    static {
        poolInit();
    }

    private static void createDruidPool() {
        if (ds_core != null)
            return;
        Properties pp_core;
        try {
            //加载properties配置文件
            pp_core = PropertiesManager.LOAD_PROPERTIES("druid.properties");
            //数据库连接池配置 使用DruidDataSourceFactory工厂来创建Druid连接池
            ds_core = DruidDataSourceFactory.createDataSource(pp_core);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (ds_core == null)
            createDruidPool();
    }

    /**
     * 获取数据库连接Connection
     *
     * @return
     */
    public static Connection getCoreConnection() {
        try {
            return ds_core.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 释放资源
    public static void release(Connection conn, Statement stat, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                    stat = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                        conn = null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void release(Connection conn, Statement stat) {
        release(conn, stat, null);
    }
}
