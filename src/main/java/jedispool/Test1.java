package jedispool;

import redis.clients.jedis.Jedis;

/**
 * redis 主从，哨兵，集群
 * https://blog.csdn.net/liuchuanhong1/article/details/53206028
 * https://www.cnblogs.com/lgeng/p/6623336.html
 * https://www.cnblogs.com/xifenglou/p/8372447.html
 */
public class Test1 {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolManager.getJedis();
        System.out.println(jedis.info());
    }
}
