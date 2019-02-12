package distributedLock.zookeeper.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BaseDistributedLockZookeeper {

    private CuratorFramework client; // zookeeper客户端
    private String path; // 全路径
    private String basePath; // 所有PERSISTENT锁节点的根位置
    private String lockName; // 锁名前缀，即临时节点
    private static final Integer MAX_RETRY_COUNT = 10; // 最大重试次数

    public BaseDistributedLockZookeeper() {
    }

    public BaseDistributedLockZookeeper(CuratorFramework client, String path, String lockName) {
        this.client = client;
        this.basePath = path;
        this.path = path.concat("/").concat(lockName);
        this.lockName = lockName;
    }

    /**
     * 删除路径
     */
    private void deleteOurPath(String path) throws Exception {
        client.delete().guaranteed().forPath(path);
    }

    /**
     * 创建路径
     */
    private String createLockNode(CuratorFramework client, String path, CreateMode withMode) throws Exception {
        return client.create().withMode(withMode).forPath(path);
    }

    /**
     * 是否存在路径
     */
    private boolean existLockNode(CuratorFramework client, String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        return stat != null ? true : false;
    }

    /**
     * 获取锁的核心方法
     *
     * @param startMillis
     * @param millisToWait
     * @param ourPath
     * @return
     * @throws Exception
     */
    private boolean waitToLock(long startMillis, Long millisToWait, String ourPath) throws Exception {
        boolean haveTheLock = false;
        boolean doDelete = false;

        try {
            while (!haveTheLock) {
                // 该方法实现获取locker节点下的所有顺序节点，并且从小到大排序
                List<String> children = getSortedChildren();
                String sequenceNodeName = ourPath.substring(basePath.length() + 1);

                // 计算刚才客户端创建的顺序节点在locker的所有子节点中排序位置，如果是排序为0，则表示获取到了锁
                int ourIndex = children.indexOf(sequenceNodeName);

                // 如果在getSortedChildren中没有找到之前创建的[临时]顺序节点，这表示可能由于网络闪断而导致
                // Zookeeper认为连接断开而删除了我们创建的节点，此时需要抛出异常，让上一级去处理
                // 上一级的做法是捕获该异常，并且执行重试指定的次数 见后面的 attemptLock方法
                if (ourIndex < 0) {
                    throw new Exception("节点没有找到: " + sequenceNodeName);
                }

                // 如果当前客户端创建的节点在locker子节点列表中位置大于0，表示其它客户端已经获取了锁
                // 此时当前客户端需要等待其它客户端释放锁，
                boolean isGetTheLock = ourIndex == 0;

                // 如何判断其它客户端是否已经释放了锁？从子节点列表中获取到比自己次小的哪个节点，并对其建立监听
                String pathToWatch = isGetTheLock ? null : children.get(ourIndex - 1);

                if (isGetTheLock) {
                    haveTheLock = true;
                } else {
                    // 如果次小的节点被删除了，则表示当前客户端的节点应该是最小的了，所以使用CountDownLatch来实现等待
                    String previousSequencePath = basePath.concat("/").concat(pathToWatch);
                    final CountDownLatch latch = new CountDownLatch(1);
                    final IZkDataListener previousListener = new IZkDataListener() {
                        // 次小节点删除事件发生时，让countDownLatch结束等待
                        // 此时还需要重新让程序回到while，重新判断一次！
                        public void handleDataDeleted(String dataPath) throws Exception {
                            latch.countDown();
                        }

                        public void handleDataChange(String dataPath, Object data) throws Exception {
                            // ignore
                        }
                    };

                    try {
                        //如果节点不存在会出现异常
                        client.subscribeDataChanges(previousSequencePath, previousListener);

                        if (millisToWait != null) {
                            millisToWait -= (System.currentTimeMillis() - startMillis);
                            startMillis = System.currentTimeMillis();
                            if (millisToWait <= 0) {
                                doDelete = true;    // timed out - delete our node
                                break;
                            }

                            latch.await(millisToWait, TimeUnit.MICROSECONDS);
                        } else {
                            latch.await();
                        }

                    } catch (Exception e) {
                        //ignore
                    } finally {
                        client.unsubscribeDataChanges(previousSequencePath, previousListener);
                    }
                }
            }
        } catch (Exception e) {
            // 发生异常需要删除节点
            doDelete = true;
            throw e;

        } finally {
            // 如果需要删除节点
            if (doDelete) {
                deleteOurPath(ourPath);
            }
        }
        return haveTheLock;
    }

    /**
     * 获取
     */
    private String getLockNodeNumber(String str, String lockName) {
        int index = str.lastIndexOf(lockName);
        if (index >= 0) {
            index += lockName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    /**
     * 获取排序的子节点
     */
    private List<String> getSortedChildren() throws Exception {
        try {
            List<String> children = client.getChildren().forPath(basePath);
            Collections.sort(
                    children,
                    new Comparator<String>() {
                        public int compare(String lhs, String rhs) {
                            return getLockNodeNumber(lhs, lockName).compareTo(getLockNodeNumber(rhs, lockName));
                        }
                    }
            );
            return children;

        } catch (Exception e) {
            createLockNode(client, basePath, CreateMode.PERSISTENT);
            return getSortedChildren();
        }
    }

    /**
     * 释放锁
     */
    protected void releaseLock(String lockPath) throws Exception {
        deleteOurPath(lockPath);
    }

    /**
     * 尝试获取锁
     *
     * @param timeout 超时时间
     * @param unit    timeout参数的单位
     */
    protected String attemptLock(long timeout, TimeUnit unit) throws Exception {
        final long startMillis = System.currentTimeMillis();
        final Long millisToWait = (unit != null) ? unit.toMillis(timeout) : null;
        String ourPath = null;
        boolean hasTheLock = false;
        boolean isDone = false;
        int retryCount = 0;

        // 网络闪断需要重试一试
        while (!isDone) {
            isDone = true;
            try {
                // createLockNode用于在locker（basePath持久节点）下创建客户端要获取锁的[临时]顺序节点
                ourPath = createLockNode(client, path, CreateMode.EPHEMERAL_SEQUENTIAL);
                // 该方法用于判断自己是否获取到了锁，即自己创建的顺序节点在locker的所有子节点中是否最小
                // 如果没有获取到锁，则等待其它客户端锁的释放，并且稍后重试直到获取到锁或者超时
                hasTheLock = waitToLock(startMillis, millisToWait, ourPath);
            } catch (Exception e) {
                if (retryCount++ < MAX_RETRY_COUNT) {
                    isDone = false;
                } else {
                    throw e;
                }
            }
        }
        if (hasTheLock) {
            return ourPath;
        }
        return null;
    }
}
