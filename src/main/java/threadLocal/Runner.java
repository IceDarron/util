package threadLocal;

public class Runner implements Runnable {

    private Target target;

    public Runner() {
    }


    public Runner(Target target) {
        this.target = target;
    }

    @Override
    public void run() {
        target.addThreadLocal();
        target.addInteger();
        target.addStaticInteger();
    }
}
