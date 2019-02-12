package distributedLock.zookeeper.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMultiThread {

    public static int threadCount = 5; // 线程数
    public static ExecutorService executorService = Executors.newCachedThreadPool();//线程池

    public static void main(String[] args) {
        // 创建锁节点
        final String lock_node = "/ROOT_LOCK";
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181", new ExponentialBackoffRetry(2000, 3));
        client.start();
        try {
            Stat stat = client.checkExists().forPath(lock_node);
            if (stat == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(lock_node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println("start executor:" + System.currentTimeMillis());
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(new Runner());
            }
            System.out.println("end executor:" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 执行完测试后，关闭程序
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                List list = client.getChildren().forPath(lock_node);
                if (list.size() == 0) {
                    client.close();
                    executorService.shutdown();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
