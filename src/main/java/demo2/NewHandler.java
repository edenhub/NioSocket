package demo2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by adam on 2015/5/2.
 */
public class NewHandler extends Thread {
    private SelectionKey serverSocketChannel;
    private final static Logger logger = Logger.getLogger(NewHandler.class.getName());

    public NewHandler(SelectionKey serverSocketChannel){
        this.serverSocketChannel = serverSocketChannel;
    }
    @Override
    public void run() {
        try {
            execute((ServerSocketChannel) serverSocketChannel.channel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void execute(ServerSocketChannel serverSocketChannel) throws IOException {
        SocketChannel socketChannel = null;
        try {
            socketChannel = serverSocketChannel.accept();
            MyRequestObject myRequestObject = receiveData(socketChannel);
            logger.log(Level.INFO, myRequestObject.toString());

            MyResponseObject myResponseObject = new MyResponseObject(
                    "response for " + myRequestObject.getName(),
                    "response for " + myRequestObject.getValue());
            sendData(socketChannel, myResponseObject);
            logger.log(Level.INFO, myResponseObject.toString());
        } finally {
            try {
                socketChannel.close();
            } catch(Exception ex) {}
        }
    }

    private static MyRequestObject receiveData(SocketChannel socketChannel) throws IOException {
        MyRequestObject myRequestObject = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            byte[] bytes;
            int size = 0;
            while ((size = socketChannel.read(buffer)) >= 0) {
                buffer.flip();
                bytes = new byte[size];
                buffer.get(bytes);
                baos.write(bytes);
                buffer.clear();
            }
            bytes = baos.toByteArray();
            System.out.println(new String(bytes)+" "+new Date());
            Object obj = SerializableUtil.toObject(bytes);
            myRequestObject = (MyRequestObject)obj;
        } finally {
            try {
                baos.close();
            } catch(Exception ex) {}
        }
        return myRequestObject;
    }

    private static void sendData(SocketChannel socketChannel, MyResponseObject myResponseObject) throws IOException {
        byte[] bytes = SerializableUtil.toBytes(myResponseObject);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        socketChannel.write(buffer);
    }
}
