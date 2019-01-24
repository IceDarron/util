package snowflake;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class Runner implements Callable {

    private SnowflakeIdWorker snowflakeIdWorker;

    private CountDownLatch countDownLatch;

    private Long createTime;

    private AtomicLong atomicLong;

    public Runner() {
    }

    public Runner(CountDownLatch countDownLatch, SnowflakeIdWorker snowflakeIdWorker, Long createTime, AtomicLong atomicLong) {
        this.snowflakeIdWorker = snowflakeIdWorker;
        this.countDownLatch = countDownLatch;
        this.createTime = createTime;
        this.atomicLong = atomicLong;
    }

    @Override
    public Object call() throws Exception {
        Long currentTime = System.currentTimeMillis();
        Long threadId = Thread.currentThread().getId();
        Long id = 0L;
        while (currentTime - createTime <= 100) {
            snowflakeIdWorker.init(0, threadId);
            id = snowflakeIdWorker.nextId();
            System.out.println(Long.toBinaryString(id));
            atomicLong.incrementAndGet();
            currentTime = System.currentTimeMillis();
        }
        countDownLatch.countDown();
        return threadId;
    }
}