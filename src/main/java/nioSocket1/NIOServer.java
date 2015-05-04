package nioSocket1;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Created by adam on 2015/5/2.
 */
public class NIOServer extends Thread {
    private InetSocketAddress socketAddress;
    private static Logger logger;
    private volatile boolean shutDown = false;
    private NIOHandler handler = new NIOHandler() {
        @Override
        public void handleAccept(SelectionKey selectionKey) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            logger.info("In HandleAccept" + new Date()+ socketChannel.socket().getLocalSocketAddress());

            socketChannel.configureBlocking(false);
            socketChannel.register(selectionKey.selector(),SelectionKey.OP_READ);
            logger.info("In HandleReadable finish" + new Date()+ socketChannel.socket().getLocalSocketAddress());
        }

        @Override
        public void handleReadable(SelectionKey selectionKey) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            logger.info("In HandleReadable" + new Date()+ socketChannel.socket().getLocalSocketAddress());
            Socket socket = socketChannel.socket();
            BufferedInputStream br = new BufferedInputStream(socket.getInputStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int ch;
            while( ( ch = br.read(buffer))!= -1 ){
                baos.write(buffer);
            }

            System.out.println(baos.toString());
            logger.info("In HandleReadable finish" + new Date()+ socketChannel.socket().getLocalSocketAddress());
        }

        @Override
        public void handleWritable(SelectionKey selectionKey) throws IOException {
            String str = "From Serve : "+new Date();
            ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());

            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            logger.info("In HandleWritable" + new Date()+ socketChannel.socket().getLocalSocketAddress());
            socketChannel.write(byteBuffer);
            logger.info("In HandleReadable finish" + new Date()+ socketChannel.socket().getLocalSocketAddress());
        }
    };
    public NIOServer(int port){
        socketAddress = new InetSocketAddress(port);
        logger = Logger.getLogger(NIOServer.class.getName());
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(socketAddress);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().setReuseAddress(true);

            SelectionKey sk = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
            logger.info("Server : "+socketAddress.toString()+" has started ... ");

            while(!shutDown){
                int receiveKeys = selector.select();
                if (receiveKeys > 0){
                    logger.info("Receive keys : "+receiveKeys+" "+new Date());

                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while(iter.hasNext()){
                        SelectionKey key = iter.next();

                        if (key.isAcceptable()) handler.handleAccept(key);
                        else if (key.isReadable()) handler.handleReadable(key);
                        else if (key.isWritable()) handler.handleWritable(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Thread thread = new NIOServer(8888);
        thread.start();
    }
}
