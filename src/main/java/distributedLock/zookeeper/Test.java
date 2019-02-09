package distributedLock.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class Test {

    public static void main(String[] args) {
        // 创建zookeeper客户端
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", retryPolicy);
        client.start();

        // zookeeper通讯测试
//        System.out.println(client.checkExists());
//        System.out.println(client.getState());
//        try {
//           client.create().forPath("/test");
//          client.create().forPath("/test1", "init".getBytes());
//            client.setData().forPath("/test1","data".getBytes());
//            System.out.println(client.getData().forPath("/test1"));
//            System.out.println(client.checkExists().forPath("/test1"));
//            System.out.println(client.getChildren().forPath("/test1"));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        // zookeeper分布式锁测试
        final int sleepTime = 5000;
        IDistributedLockZookeeper iDistributedLockZookeeper = new LockZookeeperImpl(client, "TEST_DISTRIBUTED_LOCK", new ICallback() {
            public Object onGetLock() throws InterruptedException {
                System.out.println(Thread.currentThread().getName() + ":getLock");
                Thread.currentThread().sleep(sleepTime);
                System.out.println(Thread.currentThread().getName() + ":sleeped:" + sleepTime);
                return null;
            }

            public Object onTimeout() throws InterruptedException {
                System.out.println(Thread.currentThread().getName() + ":timeout");
                return null;
            }
        });
        int getLock = 0;
        try {
            getLock = iDistributedLockZookeeper.acquire(new Long(1000L), TimeUnit.MILLISECONDS);
            if (getLock == 1) {
                System.out.println(((LockZookeeperImpl) iDistributedLockZookeeper).getiCallback().onGetLock());
            } else {
                System.out.println(((LockZookeeperImpl) iDistributedLockZookeeper).getiCallback().onTimeout());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (getLock == 1) {
                    iDistributedLockZookeeper.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
