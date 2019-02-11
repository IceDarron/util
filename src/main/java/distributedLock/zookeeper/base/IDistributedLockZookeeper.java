package distributedLock.zookeeper.base;

import java.util.concurrent.TimeUnit;

/**
 * https://www.jianshu.com/p/f430182c2b8e  curator api
 * https://www.jianshu.com/p/70151fc0ef5d  Curator使用详解
 * https://blog.csdn.net/panamera918/article/details/80196762  基于zookeeper实现分布式锁
 * https://github.com/yujiasun/Distributed-Kit  锁实现
 * https://github.com/ruanjianlxm/distributedLock  锁实现
 * https://www.jianshu.com/p/70151fc0ef5d  Curator使用详解
 * https://blog.csdn.net/panamera918/article/details/80196762  原理
 * https://www.cnblogs.com/lishijia/p/5486494.html  基于zookeeper分布式全局序列分布式锁详解
 * https://www.jianshu.com/p/5fa6a1464076 curator  分布式锁InterProcessMutex
 * https://www.cnblogs.com/shileibrave/p/9850637.html  Curator分布式锁
 *
 * zookeeper分布式锁思路：
 *
 * 概括：获取分布式锁的时候，在【LOCK节点-持久节点】下创建【临时顺序节点】，释放锁的时候删除该【临时顺序节点】。
 *
 * 1.当一个客户端请求过来，在【LOCK节点】下创建自己的【临时顺序节点】。
 *
 * 2.查看LOCK节点所有子节点，不需要监控所有节点。对所有子节点排序，如果序号最小，则认为该客户端获取了锁。
 *
 * 3.如果发现创建的【临时顺序节点】不是最小的，说明无法获取到锁，则监控比自己小的节点（注册监听器）。
 *
 * 4.如果这个被监控节点被删除，则客户端的收到相应通知，获取锁。
 * <p>
 *
 * zookeeper节点创建模式：
 * PERSISTENT：持久化
 * PERSISTENT_SEQUENTIAL：持久化并且带序列号
 * EPHEMERAL：临时
 * EPHEMERAL_SEQUENTIAL：临时并且带序列号
 * <p>
 *
 * zookeeper api：
 * 原生 zkclient curator
 * 本文选择 curator
 * <p>
 *
 */
public interface IDistributedLockZookeeper {
    /**
     * 获取锁，如果没有得到就等待
     */
    void acquire() throws Exception;

    /**
     * 获取锁，直到超时
     *
     * @param timeout 超时时间
     * @param unit timeout参数的单位
     * @throws Exception
     * @return是否获取到锁
     */
    boolean acquire(long timeout, TimeUnit unit) throws Exception;

    /**
     * 释放锁
     *
     * @throws Exception
     */
    void release() throws Exception;
}
