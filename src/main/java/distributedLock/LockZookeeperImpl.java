package distributedLock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * https://github.com/yujiasun/Distributed-Kit
 * https://github.com/ruanjianlxm/distributedLock
 */
public class LockZookeeperImpl implements IDistributedLock {

    /**
     * 线程池
     * 用于清理临时节点
     */
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    /**
     * 所有PERSISTENT锁节点的根位置
     */
    public static final String ROOT_PATH = "/ROOT_LOCK/";

    /**
     * 每次延迟清理PERSISTENT节点的时间  Unit:MILLISECONDS
     */
    private long delayTimeForClean = 1000;

    /**
     * zk 共享锁实现
     */
    private InterProcessMutex interProcessMutex = null;

    /**
     * 锁的ID,对应zk一个PERSISTENT节点,下挂EPHEMERAL节点.
     */
    private String path;

    /**
     * zk的客户端
     */
    private CuratorFramework client;



    public LockZookeeperImpl(CuratorFramework client, String lockId) {
        this.client = client;
        this.path = ROOT_PATH + lockId;
        this.interProcessMutex = new InterProcessMutex(client, this.path);
    }

    static class Cleaner implements Runnable {
        CuratorFramework client;
        String path;

        public Cleaner(CuratorFramework client, String path) {
            this.client = client;
            this.path = path;
        }

        public void run() {
            try {
                List list = client.getChildren().forPath(path);
                if (list == null || list.isEmpty()) {
                    client.delete().forPath(path);
                }
            } catch (KeeperException.NoNodeException e1) {
                //nothing
            } catch (KeeperException.NotEmptyException e2) {
                //nothing
            } catch (Exception e) {
                e.printStackTrace();//准备删除时,正好有线程创建锁
            }
        }
    }

    @Override
    public int getLock(String lockKey, String requestId, Long acquireTime, Integer expireTime) {

        try {
            return interProcessMutex.acquire(timeout, unit);
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int releaseLock(String lockKey, String requestId) {
        try {
            interProcessMutex.release();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            executorService.schedule(new Cleaner(client, path), delayTimeForClean, TimeUnit.MILLISECONDS);
        }
        return 0;
    }
}
