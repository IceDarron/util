package distributedLock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * https://www.cnblogs.com/linjiqin/p/8003838.html
 * https://blog.csdn.net/xlgen157387/article/details/79036337
 */
public interface IDistributedLock {

    /**
     * 获取分布式锁
     *
     * @param lockKey     锁标识 用于确定唯一锁
     * @param requestId   请求标识 用于确定请求。保证加锁与解锁为同一请求。保证可重入。
     * @param acquireTime 尝试请求时间
     * @param expireTime  超期时间
     * @return 是否获取成功 1=成功 0=失败
     */
    int getDistributedLock(String lockKey, String requestId, Long acquireTime, Integer expireTime);

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    int releaseDistributedLock(String lockKey, String requestId);

    /**
     * 获取锁，如果没有得到就等待
     */
    public void acquire() throws Exception;


    /**
     * 获取锁，直到超时
     *
     * @param time 超时时间
     * @param unit time参数的单位
     * @throws Exception
     * @return是否获取到锁
     */
    public boolean acquire(long time, TimeUnit unit) throws Exception;


    /**
     * 释放锁
     *
     * @throws Exception
     */
    public void release() throws Exception;
}
