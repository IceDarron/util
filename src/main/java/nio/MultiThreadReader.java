package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiThreadReader implements Runnable {
    private FileChannel channel;
    private long startIndex;
    private long endIndex;
    private long rSize;

    public MultiThreadReader(FileChannel channel, long startIndex, long rSize) {
        this.channel = channel;
        this.startIndex = startIndex > 0 ? startIndex - 1 : startIndex;
        this.rSize = rSize;
        this.endIndex = startIndex + rSize;
    }

    public void run() {
        readByLine();
    }

    /**
     * 按行读取文件实现逻辑
     *
     * @return
     */
    public void readByLine() {
        try {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            channel.position(startIndex);
            int bytesRead = channel.read(buf);
            long totalRead = startIndex;
            while (bytesRead != -1 || totalRead <= endIndex) {
                totalRead += bytesRead;
                System.out.println("Read " + bytesRead);
                // 从写模式切换到读模式
                buf.flip();

                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }

                // 准备好再次被写入
                buf.clear();
                bytesRead = channel.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 功能实现：核心类java.util.concurrent.CountDownLatch
     * CountDownLatch : 一个线程(或者多个)， 等待另外N个线程完成某个事情之后才能执行。本质上来讲就是一个计数器
     * 其核心还是AQS，在此不多赘述。
     */
    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(4); // 设置线程数
        ExecutorService executorService = Executors.newCachedThreadPool();//线程池



    }
}
