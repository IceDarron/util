package singleton;

/**
 * 饿汉模式
 * Author Darron 2016.3.18
 * 饿汉模式，即一旦类加载时就实例化了单例，所以不存在多个实例的情况，依赖ClassLoader的特性，其无疑是线程安全的。
 */
public class Singleton2 {

    private static Singleton2 singleton = new Singleton2();

    private Singleton2() {
        System.out.println("init singleton");
    }

    public static Singleton2 getInstance() {
        return singleton;
    }
}
