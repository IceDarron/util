package distributedLock;

import jedispool.JedisPoolManager;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 基于redis的分布式锁
 *
 * 应该只适用于单节点，集群中某个节点挂了无法处理。
 *
 * 对于获取锁后没有处理完就过期导致释放失败，甚至是其他客户端获取到锁，也没有处理。
 *
 * 不可重入，但是可以通过事务及唯一标示请求标识进行处理。
 *
 * @author IceDarron
 * @since 20190117
 */
public class LockRedisImpl implements IDistributedLock {

    private static final String LOCK_SUCCESS = "OK";
    // NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key if it already exist.
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_IF_EXIST = "XX";
    // EX|PX, expire time units: EX = seconds; PX = milliseconds
    private static final String SET_EXPIRE_TIME_SECOND = "EX";
    private static final String SET_EXPIRE_TIME_MILLISECOND = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    @Override
    public int getDistributedLock(String lockKey, String requestId, Long acquireTime, Integer expireTime) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolManager.getJedis();
            // 尝试获取锁
            String result;
            Long end = System.currentTimeMillis() + acquireTime;
            while (System.currentTimeMillis() < end) {
                result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_EXPIRE_TIME_MILLISECOND, expireTime);
                if (LOCK_SUCCESS.equals(result)) {
                    return 1;
                }
                // 不要频繁自旋
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public int releaseDistributedLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = null;
        try {
            jedis = JedisPoolManager.getJedis();
            Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
