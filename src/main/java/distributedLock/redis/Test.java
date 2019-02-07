package distributedLock.redis;

import jedispool.JedisPoolManager;
import redis.clients.jedis.Jedis;

public class Test {

    public static void main(String[] args) {
        // redis分布式锁测试
        Jedis jedis = JedisPoolManager.getJedis();
        System.out.println(jedis.set("TEST_DISTRIBUTED_LOCK", "1", "NX", "PX", 5000));
        System.out.println(jedis.set("TEST_DISTRIBUTED_LOCK", "2", "NX", "PX", 5000));
        IDistributedLockRedis iDistributedLock = new LockRedisImpl();
        System.out.println(iDistributedLock.getLock("TEST_DISTRIBUTED_LOCK", Thread.currentThread().getName(), 500L, 5000));
        System.out.println(iDistributedLock.releaseLock("TEST_DISTRIBUTED_LOCK", Thread.currentThread().getName()));
    }
}
