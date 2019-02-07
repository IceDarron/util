package distributedLock.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class TestMultiThread {

    public static int threadCount = 10; // 线程数
    public static ExecutorService executorService = Executors.newCachedThreadPool();//线程池
    public static String lockKey = "TEST_DISTRIBUTED_LOCK";
    public static Long acquireTime = 500L;
    public static Integer expireTime = 5000;

    // 测试是否有抢占锁的行为
    public static AtomicInteger atomicInteger = new AtomicInteger(threadCount);

    public static void main(String[] args) {
        try {
            System.out.println("start executor:" + System.currentTimeMillis());
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(new Runner(lockKey, acquireTime, expireTime));
            }
            System.out.println("end executor:" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        executorService.shutdown();
    }
}
