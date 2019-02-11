package distributedLock.zookeeper.base;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

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
    }
}
