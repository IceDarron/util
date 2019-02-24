package threadLocal;

public class Target {

    private ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    private Long integer;

    public static Long staticInteger;

    public Target() {
        initValus();
    }

    private void initValus() {
        integer = 0L;
        staticInteger = 0L;
    }

    public void addThreadLocal() {
        if (threadLocal.get() == null) {
            threadLocal.set(0L);
        }
        System.out.println(Thread.currentThread().getId() + "before threadLocal:" + threadLocal.get());
        threadLocal.set(threadLocal.get() + Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getId() + "after threadLocal:" + threadLocal.get());
        System.out.println();
    }

    public void addInteger() {
        System.out.println(Thread.currentThread().getId() + "before integer:" + integer);
        integer += Thread.currentThread().getId();
        System.out.println(Thread.currentThread().getId() + "after integer:" + integer);
        System.out.println();
    }

    public void addStaticInteger() {
        System.out.println(Thread.currentThread().getId() + "before staticInteger:" + staticInteger);
        staticInteger += Thread.currentThread().getId();
        System.out.println(Thread.currentThread().getId() + "after staticInteger:" + staticInteger);
        System.out.println();
    }
}
