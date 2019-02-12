package bloomfilter;

import jedispool.JedisPoolManager;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * http://www.cnblogs.com/haippy/archive/2012/07/13/2590351.html  原理
 * https://www.cnblogs.com/devilwind/p/7374017.html 利用布隆过滤器进行统计
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(1<<10);

        List list = new ArrayList<>();
        list.add(true);
        System.out.println(list.contains(true));

        Jedis jedis = JedisPoolManager.getJedis();
        System.out.println(jedis.info());
    }
}
