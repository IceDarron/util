package distributedLock.zookeeper;

import java.util.concurrent.TimeUnit;

public interface IDistributedLockZookeeper {
    /**
     * 获取锁，如果没有得到就等待
     */
    void acquire() throws Exception;

    /**
     * 获取锁，直到超时
     *
     * @param timeout 超时时间
     * @param unit time参数的单位
     * @throws Exception
     * @return是否获取到锁
     */
    int acquire(long timeout, TimeUnit unit) throws Exception;

    /**
     * 释放锁
     *
     * @throws Exception
     */
    int release() throws Exception;
}
