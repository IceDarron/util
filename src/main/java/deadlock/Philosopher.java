package deadlock;

import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {

    private Chopstick left; // 左边的筷子

    private Chopstick right; // 右边的筷子

    public Philosopher(Chopstick left, Chopstick right) {
        this.left = left;
        this.right = right;
    }

    private void pause() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100);
    }

    private void pauseToHoldChopstick() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " thinking");
                pause();
                right.take();
                System.out.println(this + " grabbing right");
                // 当一个人拿起了一根筷子, 停顿一段时间，让别人也抢到各自右边的筷子
                pauseToHoldChopstick();
                left.take();
                System.out.println(this + " grabbing left");
                System.out.println(this + " eating");
                pause();
                // 完成之后放下筷子
                right.drop();
                System.out.println(this + " drop right");
                left.drop();
                System.out.println(this + " drop left");
            }
        } catch (InterruptedException e) {
            System.out.println(this + " exiting via interrupt");
        }
    }
}