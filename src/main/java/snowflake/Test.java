package snowflake;

import java.util.concurrent.atomic.AtomicLong;

public class Test {

    public static AtomicLong atomicLong = new AtomicLong();

    public static void main(String[] args) {

//        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();
//        snowflakeIdWorker.init(0,0);


        // 单线程测试
//        long count = 0;
//        long start = System.currentTimeMillis();
//        long end = 0L;
//        while (end - start <= 1000) {
//            snowflakeIdWorker.nextId();
//            end = System.currentTimeMillis();
//            count++;
//        }
//        System.out.println(count);

//        for (int i = 0; i < 1000; i++) {
//            long id = snowflakeIdWorker.nextId();
//            System.out.println(Long.toBinaryString(id));
//            System.out.println(id);
//        }

//        long sequenceMask = -1L ^ (-1L << 12L);
//        System.out.println(sequenceMask);
//        System.out.println(1L << 2);
//        System.out.println(-1L << 2);
//        System.out.println(1L << 5L);
//        System.out.println(-1L);

//        System.out.println(System.currentTimeMillis());
    }

}