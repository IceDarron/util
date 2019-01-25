package distributedLock;

public class LockZookeeperImpl implements IDistributedLock{

    @Override
    public int getDistributedLock(String lockKey, String requestId, Long acquireTime, Integer expireTime) {
        return 0;
    }

    @Override
    public int releaseDistributedLock(String lockKey, String requestId) {
        return 0;
    }
}
