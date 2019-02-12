package distributedLock.zookeeper.base;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;
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

        // 测试分布式锁
//        IDistributedLockZookeeper iDistributedLockZookeeper = new LockZookeeperImpl(client, "/ROOT_LOCK");
//        try {
//            iDistributedLockZookeeper.acquire();
//            Thread.sleep(10000);
//            iDistributedLockZookeeper.release();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);

            for (int i = 0; i < 100; i++) {
                System.out.println(i);
                System.out.println(countDownLatch.await(1000, TimeUnit.MILLISECONDS));
                if (i == 3) {
                    countDownLatch.countDown();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
