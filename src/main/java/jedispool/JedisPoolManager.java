package jedispool;

import druiddatasource.PropertiesManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class JedisPoolManager {

    private static Pool<Jedis> sentinelPool;

    static {
        poolInit();
    }

    private static void createJedisPool() {
        //利用双端检测方式来保证只有一个jedispool
        if (sentinelPool != null)
            return;

        // 加载配置文件
        Properties prop = PropertiesManager.LOAD_PROPERTIES("redis.properties");
        int timeout = JedisUtils.getInt(prop, "timeout", 5000);
        // 是否单节点
        boolean isSingle = JedisUtils.getBoolean(prop, "isSingle", true);
        String host = JedisUtils.getString(prop,"host", "127.0.0.1");
        int port = JedisUtils.getInt(prop, "port", 6379);
        String masterName = JedisUtils.getString(prop,"masterName", "mymaster");
        String password = JedisUtils.getString(prop,"password", null);
        Set<String> sentinelSet = new HashSet<String>();
        if (prop.containsKey("sentinels")) {
            String sentinels = prop.getProperty("sentinels");
            String[] hostList = sentinels.split(";");
            for (String h : hostList) {
                sentinelSet.add(h);
            }
        }

        // 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(JedisUtils.getInt(prop, "maxTotal", 20));
        config.setMaxIdle(JedisUtils.getInt(prop, "maxIdle", 20));
        config.setMinIdle(JedisUtils.getInt(prop, "minIdle", 5));
        config.setTestOnBorrow(JedisUtils.getBoolean(prop, "testOnBorrow", false));
        config.setTestOnReturn(JedisUtils.getBoolean(prop, "testOnReturn", false));
        config.setMaxWaitMillis(JedisUtils.getLong(prop, "maxWaitMillis", 5000L));
        config.setTestOnReturn(JedisUtils.getBoolean(prop, "testWhileIdle", true));


        if (isSingle) {
            if (JedisUtils.isNull(password)) {
                //单机无密码
                sentinelPool = new JedisPool(config, host, port, timeout);
            } else {
                //单机有密码
                sentinelPool = new JedisPool(config, host, port, timeout, password);
            }
        } else {
            if (JedisUtils.isNull(password)) {
                //集群无密码
                sentinelPool = new JedisSentinelPool(masterName, sentinelSet, config, timeout);
            } else {
                //集群有密码
                sentinelPool = new JedisSentinelPool(masterName, sentinelSet, config, timeout, password);
            }
        }
    }

    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void poolInit() {
        if (sentinelPool == null)
            createJedisPool();
    }

    /**
     * 获取一个jedis 对象
     *
     * @return
     */
    public static Jedis getJedis() {
        return sentinelPool.getResource();
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
        sentinelPool.destroy();
    }
}
