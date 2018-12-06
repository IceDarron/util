package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioUtils {

    // 读文件buffer大小
    private final static Integer bufferReadSize = 1024;

    // 写文件buffer大小
    private final static Integer bufferWriteSize = 1024;

    /**
     * 将文件内容输出到控制台
     *
     * @param filePath 文件绝对路径
     */
    public static void readFileToConsole(String filePath) {
        // 读取文件
        RandomAccessFile aFile = null;
        // 创建文件度通道channel
        FileChannel inChannel = null;
        // 创建缓存区buffer
        ByteBuffer buf = null;
        try {
            aFile = new RandomAccessFile(filePath, "r");
            inChannel = aFile.getChannel();
            buf = ByteBuffer.allocate(bufferReadSize);

            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {

                System.out.println("Read " + bytesRead);
                // 从写模式切换到读模式
                buf.flip();

                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }

                // 准备好再次被写入
                buf.clear();
                bytesRead = inChannel.read(buf);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inChannel.close();
                aFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将文本内容写入文件
     *
     * @param filePath 文件绝对路径
     * @param content  写入的文本内容
     */
    public static void writeContentToFile(String filePath, String content) {
        // 读取文件
        RandomAccessFile aFile = null;
        // 创建文件度通道channel
        FileChannel outChannel = null;
        // 创建缓存区buffer
        ByteBuffer buf = null;

        try {
            aFile = new RandomAccessFile(filePath, "rws");
            outChannel = aFile.getChannel();
            buf = ByteBuffer.allocate(content.length());

            buf.put(content.getBytes());
            buf.flip();     //此处必须要调用buffer的flip方法
            outChannel.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outChannel.close();
                aFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
