package nio.SocketChannel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Handler {

    // 缓存区大小
    private int bufferSize = 1024;

    // 编码格式
    private String localCharset = "UTF-8";

    public Handler() {
    }

    public Handler(int bufferSize) {
        this(bufferSize, null);
    }

    public Handler(String localCharset) {
        this(-1, localCharset);
    }

    public Handler(int bufferSize, String localCharset) {
        if (bufferSize > 0) {
            this.bufferSize = bufferSize;
        }
        if (localCharset != null) {
            this.localCharset = localCharset;
        }
    }

    /**
     * 处理连接请求
     */
    public void handleAccept(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(bufferSize));
    }

    /**
     * 处理读取请求
     */
    public void handleRead(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        byteBuffer.clear();

        if (socketChannel.read(byteBuffer) == -1) {
            socketChannel.close();
        } else {
            byteBuffer.flip();
            String requestData = Charset.forName(localCharset).newDecoder().decode(byteBuffer).toString();
            System.out.println("服务端已接受到客户端请求：" + requestData);
            System.out.println(Thread.currentThread().getId());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String responseData = "服务端响应数据";
            byteBuffer = ByteBuffer.wrap(responseData.getBytes(localCharset));
            socketChannel.write(byteBuffer);
            socketChannel.close();
        }
    }
}
