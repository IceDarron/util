package jedispool;

import redis.clients.jedis.Jedis;

public class Test1 {
    public static void main(String[] args) {
        Jedis jedis = JedisPoolManager.getJedis();
        System.out.println(jedis.info());
    }
}
