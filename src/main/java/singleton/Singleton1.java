package singleton;

/**
 * 懒汉模式
 * Author Darron
 * 2016.3.18
 * 双重检验锁，进行二次判空处理的代码是必要的。
 */
public class Singleton1 {

    private static Singleton1 singleton;

    private Singleton1() { // 私有化构造方法，保证不被其他类创建实例
        System.out.println("init God");
    }

    public static Singleton1 getInstance() {
        if (singleton == null) {
            synchronized (Singleton1.class) {
                if (singleton == null) {
                    singleton = new Singleton1();
                    System.out.println("单例模式");
                }
            }
        }
        return singleton;
    }
}
