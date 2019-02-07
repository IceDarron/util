package distributedLock.zookeeper;

public interface ICallback {

    Object onGetLock() throws InterruptedException;

    Object onTimeout() throws InterruptedException;
}

