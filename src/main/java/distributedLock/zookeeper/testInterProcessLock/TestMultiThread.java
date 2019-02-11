package distributedLock.zookeeper.testInterProcessLock;

import distributedLock.zookeeper.base.Runner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMultiThread {

    public static int threadCount = 10; // 线程数
    public static ExecutorService executorService = Executors.newCachedThreadPool();//线程池

    public static void main(String[] args) {
        try {
            System.out.println("start executor:" + System.currentTimeMillis());
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(new Runner());
            }
            System.out.println("end executor:" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
