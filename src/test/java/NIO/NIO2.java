package NIO;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by adam on 2015/5/2.
 */
public class NIO2 {

    @Test
    public void test01() throws IOException {
        RandomAccessFile fromFile = new RandomAccessFile("C:\\NIO01.data","rw");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("C:\\to.data","rw");
        FileChannel toChannel = toFile.getChannel();

        toChannel.transferFrom(fromChannel,0,fromChannel.size());
        fromChannel.close();
        toChannel.close();
        toFile.close();
    }
}
