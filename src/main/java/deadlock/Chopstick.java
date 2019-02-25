package deadlock;

public class Chopstick {
    private boolean taken = false;

    public synchronized void take() throws InterruptedException {
        while (taken)   //如果被拿
            wait();
        taken = true;
    }

    public synchronized void drop() {
        taken = false;  //用完
    }
}
