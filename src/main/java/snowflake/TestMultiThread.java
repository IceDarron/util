package snowflake;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


public class TestMultiThread {

    public static void main(String[] args) {

        int threadCount = 100;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 设置线程数
        ExecutorService executorService = Executors.newCachedThreadPool();//线程池
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(); // 雪花对象
        AtomicLong atomicLong = new AtomicLong(); // 统计信息
        Long createTime = System.currentTimeMillis(); // 存活时间

        System.out.println(System.currentTimeMillis());
        // 创建多个线程执行主键获取
        for (int i = 0; i < threadCount; i++) {
            System.out.println(executorService.submit(new Runner(countDownLatch, snowflakeIdWorker, createTime, atomicLong)));
        }
        System.out.println(System.currentTimeMillis());

        try {
            countDownLatch.await(); // 等待所有磁盘计算完毕
            executorService.shutdown(); // 提交所有任务
            System.out.println(atomicLong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis());
    }


}


