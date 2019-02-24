package nio.SocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class WebClient implements Runnable {

    @Override
    public void run() {
        try {
            // 通过SocketChannel的open()方法创建一个SocketChannel对象 连接到远程服务器（连接此通道的socket）
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress("127.0.0.1", 3333));

            // 创建写数据缓存区对象
            ByteBuffer writeBuffer = ByteBuffer.allocate(128);
            writeBuffer.put(("WebClient`s msg" + Thread.currentThread().getId()).getBytes());
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            // 创建读数据缓存区对象
            ByteBuffer readBuffer = ByteBuffer.allocate(128);
            socketChannel.read(readBuffer);
            readBuffer.flip();
            String requestData = Charset.forName("UTF-8").newDecoder().decode(readBuffer).toString();
//            StringBuffer sb = new StringBuffer();
//            while (readBuffer.hasRemaining()) {
//                sb.append((char) readBuffer.get());
//            }
            System.out.println("从服务端接收到的数据：" + requestData);
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}