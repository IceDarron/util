package jedispool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

/**
 * redis 主从，哨兵，集群
 * https://blog.csdn.net/liuchuanhong1/article/details/53206028 redis sentinel部署
 * https://www.cnblogs.com/lgeng/p/6623336.html Redis单机配置多实例，实现主从同步
 * https://www.cnblogs.com/xifenglou/p/8372447.html redis主从｜哨兵｜集群模式
 * https://www.cnblogs.com/tommy-huang/p/6240083.html 搭建Redis集群
 * https://www.cnblogs.com/dadonggg/p/8628735.html redis集群节点宕机
 * https://blog.csdn.net/liqfyiyi/article/details/50894020 redis集群
 */
public class Test1 {
    public static void main(String[] args) {
//        Jedis jedis = JedisPoolManager.getJedis();
//        System.out.println(jedis.info());

        JedisCluster clusterPool = JedisPoolManager.getCluster();
        System.out.println(clusterPool.get("a"));
    }
}
