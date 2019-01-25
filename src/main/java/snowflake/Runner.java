package snowflake;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class Runner implements Callable {

    private CountDownLatch countDownLatch;

    private Long createTime;

    private AtomicLong atomicLong;

    private Map<Long, Long> base;

    public Runner() {
    }

    public Runner(CountDownLatch countDownLatch, Long createTime, AtomicLong atomicLong, Map<Long, Long> base) {
        this.countDownLatch = countDownLatch;
        this.createTime = createTime;
        this.atomicLong = atomicLong;
        this.base = base;
    }

    @Override
    public Object call() throws Exception {
        Long currentTime = System.currentTimeMillis();
        Long dataCenterId = 0L;
        Long threadId = Thread.currentThread().getId();
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(dataCenterId, threadId); // 雪花对象
//        System.out.println("create snowflake:" + Long.toBinaryString(dataCenterId) + Long.toBinaryString(threadId));
//        System.out.println("create snowflake:" + dataCenterId + threadId);
        Long id;
        while (currentTime - createTime <= 1000) {
            id = snowflakeIdWorker.nextId();
            atomicLong.incrementAndGet();
            currentTime = System.currentTimeMillis();

//            System.out.println(Long.toBinaryString(id));
//            Long value = base.get(id);
//            if (value == null) {
//                base.put(id, 1L);
//            } else {
//                base.put(id, value + 1);
//            }
        }
        countDownLatch.countDown();
        return threadId;
    }
}