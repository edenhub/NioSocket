package nioSocket1;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * Created by adam on 2015/5/2.
 */
public interface NIOHandler {

    public void handleAccept(SelectionKey selectionKey) throws IOException;

    public void handleReadable(SelectionKey selectionKey) throws IOException;

    public void handleWritable(SelectionKey selectionKey) throws IOException;
}
