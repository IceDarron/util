package distributedLock.zookeeper.testInterProcessLock;

public interface ICallback {

    Object onGetLock() throws InterruptedException;

    Object onTimeout() throws InterruptedException;
}

