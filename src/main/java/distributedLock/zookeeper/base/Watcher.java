package distributedLock.zookeeper.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @描述：第三种监听器的添加方式: Cache 的三种实现 实践
 * Path Cache：监视一个路径下1）孩子结点的创建、2）删除，3）以及结点数据的更新。
 * 产生的事件会传递给注册的PathChildrenCacheListener。
 * Node Cache：监视一个结点的创建、更新、删除，并将结点的数据缓存在本地。
 * Tree Cache：Path Cache和Node Cache的“合体”，监视路径下的创建、更新、删除事件，并缓存路径下所有孩子结点的数据。
 */
public class Watcher {

    private CountDownLatch countDownLatch;

    public Watcher() {
    }

    public Watcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    /**
     * path Cache
     * 能监听所有的子节点 且是无限监听的模式 但是指定目录下节点的子节点不再监听
     *
     * @param client zookeeper客户端
     * @param path   监控的路径
     */
    public void listenterPathCache(CuratorFramework client, String path) throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(client, path, true);
        PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("CHILD_ADDED : " + data.getPath() + "  数据:" + data.getData());
                        break;
                    case CHILD_REMOVED:
                        System.out.println("CHILD_REMOVED : " + data.getPath() + "  数据:" + data.getData());
                        countDownLatch.countDown();
                        break;
                    case CHILD_UPDATED:
                        System.out.println("CHILD_UPDATED : " + data.getPath() + "  数据:" + data.getData());
                        break;
                    default:
                        break;
                }
            }
        };
        childrenCache.getListenable().addListener(childrenCacheListener);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    }

    /**
     * Node Cache
     * 监听本节点的变化，节点可以进行修改操作，删除节点后会再次创建(空节点)
     *
     * @param client zookeeper客户端
     * @param path   监控的路径
     */
    public void listenterNodeCache(CuratorFramework client, String path) throws Exception {
        // 设置节点的cache
        final NodeCache nodeCache = new NodeCache(client, path, false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("the test node is change and result is :");
                System.out.println("path : " + nodeCache.getCurrentData().getPath());
                System.out.println("data : " + new String(nodeCache.getCurrentData().getData()));
                System.out.println("stat : " + nodeCache.getCurrentData().getStat());
            }
        });
        nodeCache.start();
    }

    /**
     * Tree Cache
     * 监控指定节点和节点下的所有的节点的变化,无限监听,可以进行本节点的删除(不在创建)
     *
     * @param client zookeeper客户端
     * @param path   监控的路径
     */
    public void listenterTreeCache(CuratorFramework client, String path) throws Exception {
        // 设置节点的cache
        TreeCache treeCache = new TreeCache(client, "/test");
        // 设置监听器和处理过程
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data != null) {
                    switch (event.getType()) {
                        case NODE_ADDED:
                            System.out.println("NODE_ADDED : " + data.getPath() + "  数据:" + new String(data.getData()));
                            break;
                        case NODE_REMOVED:
                            System.out.println("NODE_REMOVED : " + data.getPath() + "  数据:" + new String(data.getData()));
                            break;
                        case NODE_UPDATED:
                            System.out.println("NODE_UPDATED : " + data.getPath() + "  数据:" + new String(data.getData()));
                            break;

                        default:
                            break;
                    }
                } else {
                    System.out.println("data is null : " + event.getType());
                }
            }
        });
        // 开始监听
        treeCache.start();
    }
}
