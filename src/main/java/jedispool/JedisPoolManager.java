package jedispool;

import druiddatasource.PropertiesManager;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class JedisPoolManager {

    private static Pool<Jedis> pool;

    private static JedisCluster clusterPool;

    private static void createPool() {
        // 加载配置文件
        Properties prop = PropertiesManager.LOAD_PROPERTIES("redis.properties");

        // common
        int maxTotal = JedisUtils.getInt(prop, "maxTotal", 20);
        int maxIdle = JedisUtils.getInt(prop, "maxIdle", 20);
        int minIdle = JedisUtils.getInt(prop, "minIdle", 5);
        boolean testOnBorrow = JedisUtils.getBoolean(prop, "testOnBorrow", false);
        boolean testOnReturn = JedisUtils.getBoolean(prop, "testOnReturn", false);
        boolean testWhileIdle = JedisUtils.getBoolean(prop, "testWhileIdle", true);
        long maxWaitMillis = JedisUtils.getLong(prop, "maxWaitMillis", 5000L);
        int timeout = JedisUtils.getInt(prop, "timeout", 5000);
        String password = JedisUtils.getString(prop, "password", null);
        String model = JedisUtils.getString(prop, "model", null);

        // master_slave_node
        String master_slave_host = JedisUtils.getString(prop, "master_slave_node", "master_slave_node").split(":")[0];
        int master_slave_port = Integer.parseInt(JedisUtils.getString(prop, "master_slave_node", "master_slave_node").split(":")[1]);

        // sentinel
        String masterName = JedisUtils.getString(prop, "masterName", "master");
        String sentinel = JedisUtils.getString(prop, "sentinel_node", null);
        Set<String> sentinelNode = new HashSet<String>();
        String[] sentinelArray = sentinel.split(",");
        for (String s : sentinelArray) {
            sentinelNode.add(s);
        }

        // cluster
        String cluster = JedisUtils.getString(prop, "cluster_node", null);
        String[] clusterArray = cluster.split(",");
        Set<HostAndPort> clusterNode = new HashSet<>();
        for (String s : clusterArray) {
            String[] arr = s.split(":");
            clusterNode.add(new HostAndPort(arr[0], Integer.valueOf(arr[1])));
        }
        int soTimeout = JedisUtils.getInt(prop, "so_timeout", 2000);
        int maxRedirections = JedisUtils.getInt(prop, "max_redirections", 10);

        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setTestOnReturn(testWhileIdle);
        config.setMaxWaitMillis(maxWaitMillis);

        if ("master_slave".equals(model)) {
            pool = new JedisPool(config, master_slave_host, master_slave_port, timeout, password);
        } else if ("sentinel".equals(model)) {
            pool = new JedisSentinelPool(masterName, sentinelNode, config, timeout, password);
        } else if ("cluster".equals(model)) {
            clusterPool = new JedisCluster(clusterNode, timeout, soTimeout, maxRedirections, config);
        } else {
            System.out.println("请选择创建的redis模式");
        }
    }

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    public static Jedis getJedis() {
        if (pool == null) {
            synchronized (JedisPoolManager.class) {
                if (pool == null) {
                    createPool();
                    System.out.println("创建jedis连接池完毕");
                }
            }
        }
        return pool.getResource();
    }

    /**
     * 获取一个clusterPool 对象
     *
     * @return
     */
    public static JedisCluster getCluster() {
        if (clusterPool == null) {
            synchronized (JedisPoolManager.class) {
                if (clusterPool == null) {
                    createPool();
                    System.out.println("创建JedisCluster完毕");
                }
            }
        }
        return clusterPool;
    }

    /**
     * 关闭一个连接
     *
     * @param jedis
     */
    public static void close(Jedis jedis) {
        jedis.close();
    }

    /**
     * 销毁一个连接
     *
     * @param jedis
     */
    public static void destroy(Jedis jedis) {
        pool.destroy();
    }
}
