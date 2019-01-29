package distributedLock;

import jedispool.JedisPoolManager;
import redis.clients.jedis.Jedis;

public class Test {

    public static void main(String[] args) {
        Jedis jedis = JedisPoolManager.getJedis();
//        System.out.println(jedis.set("TEST_DISTRIBUTED_LOCK", "1", "NX", "PX", 5000));
//        System.out.println(jedis.set("TEST_DISTRIBUTED_LOCK", "2", "NX", "PX", 5000));


        IDistributedLock iDistributedLock = new LockRedisImpl();
        System.out.println(iDistributedLock.getDistributedLock("TEST_DISTRIBUTED_LOCK", Thread.currentThread().getName(), 500L, 5000));
        System.out.println(iDistributedLock.releaseDistributedLock("TEST_DISTRIBUTED_LOCK", Thread.currentThread().getName()));

    }
}
