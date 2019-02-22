package nio.SocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class WebServer {
    public static void main(String[] args) {
        // 1.通过ServerSocketChannel 的open()方法创建一个ServerSocketChannel对象，open方法的作用：打开套接字通道
        ServerSocketChannel ssc = null;

        // 通过ServerSocketChannelImpl的accept()方法创建一个SocketChannel对象用户从客户端读/写数据
        SocketChannel socketChannel = null;
        try {
            ssc = ServerSocketChannel.open();
            // 2.通过ServerSocketChannel绑定ip地址和port(端口号)
            ssc.socket().bind(new InetSocketAddress("127.0.0.1", 3333));
            while (true) {
                // 通过ServerSocketChannelImpl的accept()方法创建一个SocketChannel对象用户从客户端读/写数据
                socketChannel = ssc.accept();

                // 3.创建写数据的缓存区对象
                ByteBuffer writeBuffer = ByteBuffer.allocate(128);
                writeBuffer.put("hello WebClient this is from WebServer".getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);

                // 创建读数据的缓存区对象
                ByteBuffer readBuffer = ByteBuffer.allocate(128);

                // 读取缓存区数据
                socketChannel.read(readBuffer);
                StringBuffer sb = new StringBuffer();

                // 4.将Buffer从写模式变为可读模式
                readBuffer.flip();

                while (readBuffer.hasRemaining()) {
                    sb.append(readBuffer.get());
                }
                System.out.println("从客户端接收到的数据：" + sb);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
                ssc.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
