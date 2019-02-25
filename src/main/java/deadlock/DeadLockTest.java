package deadlock;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * https://blog.csdn.net/wangtaomtk/article/details/52301614  模拟死锁
 */
public class DeadLockTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        int size = 3;

        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for (int i = 0; i < size; i++)
            sticks[i] = new Chopstick();

        for (int i = 0; i < size; i++)
            exec.execute(new Philosopher(sticks[i], sticks[(i + 1) % size]));

        TimeUnit.SECONDS.sleep(50);

        // 关掉所有的线程
        exec.shutdownNow();
    }
}
