package distributedLock.zookeeper.testInterProcessLock;

import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.TimeUnit;

public class Executor {

    final int sleepTime = 5000;

    public Object execute(CuratorFramework client) {
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
        return null;
    }
}
