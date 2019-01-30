package distributedLock;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/panamera918/article/details/80196762
 * https://blog.csdn.net/zhailuxu/article/details/80658016
 */
public class LockZookeeperImpl extends AbstractLockZookeeper implements IDistributedLock {

    /**
     * 用于保存Zookeeper中实现分布式锁的节点，如名称为locker：/locker，
     * 该节点应该是持久节点，在该节点下面创建临时顺序节点来实现分布式锁
     */
    private String basePath;

    /**
     * 锁名称前缀，locker下创建的顺序节点例如都以lock-开头，这样便于过滤无关节点。
     * LOCK-00000001，LOCK-000000002
     */
    private static final String LOCK_NAME = "LOCK-";

    /**
     * 用于保存某个客户端在locker下面创建成功的顺序节点，用于后续相关操作使用（如判断）
     */
    private String ourLockPath;

    /**
     * 用于获取锁资源，通过父类的获取锁方法来获取锁
     *
     * @param time 获取锁的超时时间
     * @param unit time的时间单位
     * @throws Exception
     * @return是否获取到锁
     */
    private boolean internalLock(long time, TimeUnit unit) throws Exception {
        //如果ourLockPath不为空则认为获取到了锁，具体实现细节见attemptLock的实现
        ourLockPath = attemptLock(time, unit);
        return ourLockPath != null;

    }

    /**
     * 传入Zookeeper客户端连接对象，和basePath
     *
     * @param client   Zookeeper客户端连接对象
     * @param basePath basePath是一个持久节点
     */
    public LockZookeeperImpl(ZkClientExt client, String basePath) {
        /**调用父类的构造方法在Zookeeper中创建basePath节点，并且为basePath节点子节点设置前缀
         *同时保存basePath的引用给当前类属性*/
        super(client, basePath, LOCK_NAME);
        this.basePath = basePath;
    }

    /**
     * 获取锁，直到超时，超时后抛出异常
     */
    public void acquire() throws Exception {
        //-1表示不设置超时时间，超时由Zookeeper决定
        if (!internalLock(-1, null)) {
            throw new IOException("连接丢失!在路径:'" + basePath + "'下不能获取锁!");
        }
    }

    /**
     * 获取锁，带有超时时间
     */
    public boolean acquire(long time, TimeUnit unit) throws Exception {
        return internalLock(time, unit);
    }

    /**
     * 释放锁
     */
    public void release() throws Exception {
        releaseLock(ourLockPath);
    }

    @Override
    public int getDistributedLock(String lockKey, String requestId, Long acquireTime, Integer expireTime) {
        return 0;
    }

    @Override
    public int releaseDistributedLock(String lockKey, String requestId) {
        return 0;
    }
}
