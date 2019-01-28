package distributedLock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestMultiThread {

    public static void main(String[] args) {

        int threadCount = 10; // 线程数
        ExecutorService executorService = Executors.newCachedThreadPool();//线程池
        String lockKey = "TEST_DISTRIBUTED_LOCK";
        Long acquireTime = 500L;
        Integer expireTime = 5000;


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
