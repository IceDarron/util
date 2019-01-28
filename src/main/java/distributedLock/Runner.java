package distributedLock;

import java.util.concurrent.Callable;

public class Runner implements Callable {

    String lockKey;

    String requestId;

    Long acquireTime;

    Integer expireTime;

    int result;

    public Runner() {
    }

    public Runner(String lockKey, Long acquireTime, Integer expireTime) {
        this.lockKey = lockKey;
        this.acquireTime = acquireTime;
        this.expireTime = expireTime;
    }

    @Override
    public Object call() throws Exception {
        IDistributedLock iDistributedLock = new LockRedisImpl();
        requestId = Thread.currentThread().getName();

        while (!"1".equals(result)) {
            result = iDistributedLock.getDistributedLock(lockKey, requestId, acquireTime, expireTime);
            if ("0".equals(result)) {
                System.out.println("get lock, result:" + result + "    requestId:" + requestId);
                System.out.println("wait to get lock");
            } else {
                System.out.println("get lock, result:" + result + "    requestId:" + requestId);
                System.out.println("use lock..., requestId:" + requestId);
                Thread.sleep(30000);
                iDistributedLock.releaseDistributedLock(lockKey, requestId);
                System.out.println("release lock, requestId:" + requestId);
            }
        }
        return null;
    }
}