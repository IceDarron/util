package threadLocal;

public class Test {

    public static void main(String[] args) {

        // 每个线程单独创建Target对象
        // 每个Target对象数据都是独立的，static会共享，但是不保证static修饰的数据能原子性
//        System.out.println("Start");
//        for (int i = 0; i < 10; i++) {
//            new Thread(new Runner(new Target())).start();
//        }
//        System.out.println("End");

        // 所有线程共用一个Target对象
        // 共用一个Target，数据都是共享的，但是ThreadLocal的数据是独立的，即会为每个线程拷贝副本。
        Target target = new Target();
        System.out.println("Start");
        for (int i = 0; i < 10; i++) {
            new Thread(new Runner(target)).start();
        }
        System.out.println("End");
    }
}
