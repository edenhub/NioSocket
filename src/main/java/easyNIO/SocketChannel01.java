package easyNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by adam on 2015/5/2.
 */
public class SocketChannel01 {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("www.baidu.com", 80));

        while(!socketChannel.finishConnect()){
            System.out.println("writing ... ");
        }

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int idx = socketChannel.read(buffer);
        while(idx != -1){
            buffer.flip();
            while(buffer.hasRemaining())
                System.out.print((char)buffer.get());
            buffer.clear();
            idx=socketChannel.read(buffer);
        }
        System.out.println("==end==");
    }
}
