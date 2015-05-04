package demo2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MyClient3 {

	private final static Logger logger = Logger.getLogger(MyClient3.class.getName());
	
	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 100; i++) {
			final int idx = 1;
            Thread.sleep(2000);
			new Thread(new MyRunnable(idx)).start();
//		}
	}
	
	private static final class MyRunnable implements Runnable {
		
		private final int idx;

		private MyRunnable(int idx) {
			this.idx = idx;
		}

		public void run() {
			SocketChannel socketChannel = null;
			try {
				socketChannel = SocketChannel.open();
				SocketAddress socketAddress = new InetSocketAddress("localhost", 1234);

				socketChannel.connect(socketAddress);

				MyRequestObject myRequestObject = new MyRequestObject("request_" + idx, "request_" + idx);
				logger.log(Level.INFO, myRequestObject.toString()+" "+Thread.currentThread().getName());
				sendData(socketChannel, myRequestObject);

				MyResponseObject myResponseObject = receiveData(socketChannel);
                logger.log(Level.INFO, myResponseObject.toString());
//
                socketChannel = SocketChannel.open();

                socketChannel.connect(socketAddress);
                MyRequestObject myRequestObject2 = new MyRequestObject("request_" + (idx+1), "request_" + (idx+1));
                logger.log(Level.INFO, myRequestObject2.toString()+" "+Thread.currentThread().getName());
                sendData(socketChannel, myRequestObject2);

                MyResponseObject myResponseObject2 = receiveData(socketChannel);
                logger.log(Level.INFO, myResponseObject2.toString());
			} catch (Exception ex) {
				logger.log(Level.SEVERE, null, ex);
			} finally {
				try {
					socketChannel.close();
				} catch(Exception ex) {}
			}
		}

		private void sendData(SocketChannel socketChannel, MyRequestObject myRequestObject) throws IOException {
			byte[] bytes = SerializableUtil.toBytes(myRequestObject);
			ByteBuffer buffer = ByteBuffer.wrap(bytes);
			socketChannel.write(buffer);
			socketChannel.socket().shutdownOutput();//通知server,client写完了
		}

		private MyResponseObject receiveData(SocketChannel socketChannel) throws IOException {
			MyResponseObject myResponseObject = null;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			try {
				ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
				byte[] bytes;
				int count = 0;
				while ((count = socketChannel.read(buffer)) >= 0) {
					buffer.flip();
					bytes = new byte[count];
					buffer.get(bytes);
					baos.write(bytes);
					buffer.clear();
				}
				bytes = baos.toByteArray();
				Object obj = SerializableUtil.toObject(bytes);
				myResponseObject = (MyResponseObject) obj;
				socketChannel.socket().shutdownInput();
			} finally {
				try {
					baos.close();
				} catch(Exception ex) {}
			}
			return myResponseObject;
		}
	}
}
