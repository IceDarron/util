package snowflake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


public class TestMultiThread {

    public static void main(String[] args) {

        int threadCount = 10; // 线程数 不知道为什么超过20就挂了 线程起不来
        CountDownLatch countDownLatch = new CountDownLatch(threadCount); // 设置线程数
        ExecutorService executorService = Executors.newCachedThreadPool();//线程池
        AtomicLong atomicLong = new AtomicLong(); // 统计信息-所有线程调用SnowflakeIdWorker.nextId()总次数
        Long createTime = System.currentTimeMillis(); // 存活时间-所有线程公用一个开始时间
        Map<Long, Long> base = new ConcurrentHashMap<>(); // 存放雪花生成的所有id-<id, 次数>

        try {
            System.out.println("start executor, time:" + System.currentTimeMillis()); // 开始启动线程
            // 创建多个线程执行主键获取
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(new Runner(countDownLatch, createTime, atomicLong, base));
            }
            System.out.println("end executor, time::" + System.currentTimeMillis());
            countDownLatch.await(); // 等待所有线程完毕
            executorService.shutdown(); // 提交所有任务
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 统计去重id
        int count = 0;
        for (Map.Entry entry : base.entrySet()) {
            if ((Long) entry.getValue() == 1) {
                count++;
            }
        }
        System.out.println("all executor is over, time:" + System.currentTimeMillis());
        System.out.println("call snowflake num:" + atomicLong);
        System.out.println("id pure num:" + base.size());
        System.out.println("id no duplicate num:" + count);
    }


}


