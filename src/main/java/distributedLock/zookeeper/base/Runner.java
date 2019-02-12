package distributedLock.zookeeper.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Runner implements Callable {

    public Runner() {
    }

    @Override
    public Object call() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(2000, 3));
        client.start();

        IDistributedLockZookeeper locker = new LockZookeeperImpl(client, "/ROOT_LOCK");
        try {
            boolean lockFlag = locker.acquire(30000, TimeUnit.MILLISECONDS);
            if (lockFlag) {
                System.out.println("获得锁" + Thread.currentThread().getName());
                Thread.sleep(5000);
                locker.release();
                System.out.println("释放锁" + Thread.currentThread().getName());
            } else {
                System.out.println("没有获得锁" + Thread.currentThread().getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
        return null;
    }
}
