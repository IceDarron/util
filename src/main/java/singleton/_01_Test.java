package singleton;


public class _01_Test implements Runnable {

    public static void main(String[] args) {
        _01_Test t = new _01_Test();
        Thread thread1 = new Thread(t);
        thread1.start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Singleton1.getInstance();
    }
}
