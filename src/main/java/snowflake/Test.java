package snowflake;

public class Test {
    public static void main(String[] args) {

        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();
        snowflakeIdWorker.init(0,0);
        for (int i = 0; i < 1000; i++) {
            long id = snowflakeIdWorker.nextId();
            System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }

//        long sequenceMask = -1L ^ (-1L << 12L);
//        System.out.println(sequenceMask);
//        System.out.println(1L << 2);
//        System.out.println(-1L << 2);
//        System.out.println(1L << 5L);
//          System.out.println(-1L);
    }

}