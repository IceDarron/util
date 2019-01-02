package jedispool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolManager.getJedis();
//        System.out.println(jedis.hget("0:SYSTEM_CONFIG:RUN_MODE","WS_NAME"));
//        System.out.println(jedis.hset("rongxn","testIncrease", "0"));
//        System.out.println(jedis.hget("rongxn","testIncrease"));
//        System.out.println(jedis.hgetAll("0:SYSTEM_CONFIG:RUN_MODE"));
//        System.out.println(jedis.hincrBy("rongxn","testIncrease",1)); // 增长value
//        System.out.println(jedis.hmget("0:C_RCA_CONS_STATUS","0","1"));
//        System.out.println(jedis.hlen("0:C_RCA_CONS_STATUS"));

        // 时间差
        long l1 = 0L;
        long l2 = 0L;

        // 测试数据量
        // 100:管道=16ms 单独请求=37ms
        // 1000:管道=22ms 单独请求=331ms
        // 10000:管道=156ms 单独请求=2917ms
        // 50000:管道=472ms 单独请求=13502ms
        int count = 100;

        // 管道
        Map<Integer, Response<String>> result = new HashMap<>();
        Pipeline p = jedis.pipelined();
        l1 = System.currentTimeMillis();
        p.clear();
        for (int i = 0; i < count; i++) {
            result.put(i, p.hget("0:C_RCA_CONS_STATUS", i + ""));
        }
        p.sync();
//        System.out.println(result.get(1).get());
//        System.out.println(result.get(2).get());
        l1 = System.currentTimeMillis() - l1;
        System.out.println(l1);

        System.out.println("***********************************************");

        // 单个获取
        Map<Integer, String> singleGet = new HashMap();
        l2 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            singleGet.put(i, jedis.hget("0:C_RCA_CONS_STATUS", i + ""));
        }
        l2 = System.currentTimeMillis() - l2;
        System.out.println(l2);

        // 关闭连接
        JedisPoolManager.close(jedis);
    }
}
