package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.*;

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


    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());//线程池

    }
}
