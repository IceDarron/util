package jedispool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * redis 主从，哨兵，集群
 * https://blog.csdn.net/liuchuanhong1/article/details/53206028
 * https://www.cnblogs.com/lgeng/p/6623336.html
 * https://www.cnblogs.com/xifenglou/p/8372447.html
 * https://www.cnblogs.com/tommy-huang/p/6240083.html
 */
public class Test1 {
    public static void main(String[] args) {
//        Jedis jedis = JedisPoolManager.getJedis();
//        System.out.println(jedis.info());

        JedisCluster clusterPool = JedisPoolManager.getCluster();
        System.out.println(clusterPool.get("a"));
    }
}
