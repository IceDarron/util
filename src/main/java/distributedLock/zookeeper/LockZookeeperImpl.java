package distributedLock.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * https://github.com/yujiasun/Distributed-Kit  锁实现
 * https://github.com/ruanjianlxm/distributedLock  锁实现
 * https://www.jianshu.com/p/70151fc0ef5d  Curator使用详解
 * https://blog.csdn.net/panamera918/article/details/80196762  原理
 */
public class LockZookeeperImpl implements IDistributedLockZookeeper {

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

    /**
     * 回调
     */
    ICallback iCallback;

    public ICallback getiCallback() {
        return iCallback;
    }

    public LockZookeeperImpl(CuratorFramework client, String lockId, ICallback iCallback) {
        this.client = client;
        this.path = ROOT_PATH + lockId;
        this.iCallback = iCallback;
        this.interProcessMutex = new InterProcessMutex(client, this.path);
    }

    @Override
    public void acquire() throws Exception {

    }

    @Override
    public int acquire(long timeout, TimeUnit unit) {
        try {
            return interProcessMutex.acquire(timeout, unit) ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public int release() {
        try {
            interProcessMutex.release();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            executorService.schedule(new Cleaner(client, path), delayTimeForClean, TimeUnit.MILLISECONDS);
        }
        return 0;
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
}
