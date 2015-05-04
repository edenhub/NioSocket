package easyNIO;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by adam on 2015/5/2.
 */
public class NIO01 {

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("C:\\NIO01.data","rw");
        FileChannel fc = file.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(48);
        int idx = fc.read(buffer);
        System.out.println(idx);
        while(idx !=-1){
            buffer.flip();
            while(buffer.hasRemaining())
                System.out.print((char)buffer.get());
            buffer.clear();
            idx = fc.read(buffer);
        }

        System.out.println("==end==");
    }
}
