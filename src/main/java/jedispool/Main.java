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


        // 管道
        Map<Integer, Response<String>> result = new HashMap<>();
        Pipeline p = jedis.pipelined();
        p.clear();
        result.put(1, p.hget("0:C_RCA_CONS_STATUS", "0"));
        result.put(2, p.hget("0:C_RCA_CONS_STATUS", "1"));
        p.sync();

        System.out.println(result.get(1).get());
        System.out.println(result.get(2).get());


        JedisPoolManager.close(jedis);

    }
}
