package nio.SocketChannel;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * 多路复用
 * <p>
 * 通过Selector实现ServerSocketChannel多路复用
 */
public class MultiWebServer {

    public static void main(String[] args) {
        try {
            // 创建ServerSocketChannel通道 绑定监听端口为8080 设置为非阻塞模式
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 3333));
            serverSocketChannel.configureBlocking(false);

            // 创建选择器 服务器通道只能注册SelectionKey.OP_ACCEPT事件
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 创建处理器
            Handler handler = new Handler(1024);

            while (true) {
                // 等待请求，每次等待阻塞1s，超过时间则向下执行，若传入0或不传值，则在接收到请求前一直阻塞
                if (selector.select(1000) == 0) {
                    System.out.println("等待请求超时......");
                    continue;
                }

                System.out.println("------处理数据------");
                // 获取待处理的选择键集合
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        handler.handleAccept(selectionKey);
                    }
                    if (selectionKey.isReadable()) {
                        handler.handleRead(selectionKey);
                    }
                }
                iterator.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

