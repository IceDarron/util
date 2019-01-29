package distributedLock;

import java.util.concurrent.Callable;

import static distributedLock.TestMultiThread.atomicInteger;

public class Runner implements Callable {

    String lockKey;

    String requestId;

    Long acquireTime;

    Integer expireTime;

    int lockResult;

    int releaseResult;

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

        while (lockResult == 0) {
            lockResult = iDistributedLock.getDistributedLock(lockKey, requestId, acquireTime, expireTime);
            if (lockResult == 0) {
                System.out.println("wait to get lock, requestId:" + requestId + "    current time:" + System.currentTimeMillis());
                Thread.sleep(expireTime / 2);
            } else {
                System.out.println("use lock..., requestId:" + requestId);
                atomicInteger.decrementAndGet();
                System.out.println("decrement, requestId:" + requestId + "    total: " + atomicInteger);
                Thread.sleep(expireTime / 2);
                releaseResult = iDistributedLock.releaseDistributedLock(lockKey, requestId);
                System.out.println("release lock, result:" + releaseResult + "    requestId:" + requestId);
            }
        }
        return null;
    }
}