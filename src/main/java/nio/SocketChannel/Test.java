package nio.SocketChannel;


/**
 * https://www.cnblogs.com/doit8791/p/7461479.html  Reactor模式
 * https://blog.csdn.net/aigoogle/article/details/27699007  基于NIO多路复用实现
 */
public class Test {
    public static void main(String[] args) {
        System.out.println("Start");
        for (int i = 0; i < 10; i++) {
            new Thread(new WebClient()).start();
        }
        System.out.println("End");
    }
}
