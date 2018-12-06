package jedispool;

import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolManager.getJedis();
        System.out.println(jedis.hget("0:SYSTEM_CONFIG:RUN_MODE","WS_NAME"));
    }
}
