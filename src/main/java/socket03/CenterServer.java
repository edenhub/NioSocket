package socket03;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adam on 2015/5/2.
 */
public class CenterServer {
    private ServerSocket serverSocket;
    private ExecutorService poolService;
    private volatile boolean shutDown = false;
    public CenterServer(int port){
        try {
            serverSocket = new ServerSocket(port);
            poolService = Executors.newFixedThreadPool(20);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void service() throws IOException {
        System.err.println("Servier "+serverSocket.getInetAddress().toString()+" is running");
        while(!shutDown){
            Socket socket = serverSocket.accept();
            poolService.execute(new CenterHandler(socket));

        }
    }

    public class CenterHandler implements Runnable{
        private Socket socketFrom;
        private ObjectInputStream oin;
        private ObjectOutputStream oout;
        public CenterHandler(Socket socketFrom){
            this.socketFrom = socketFrom;
            try {
                oin = new ObjectInputStream(socketFrom.getInputStream());
                oout = new ObjectOutputStream(socketFrom.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while(true){
                try {
                    UserObj userObj = (UserObj) oin.readObject();
                    System.err.println(userObj);
                    String msg = userObj.getMsg();
                    if (msg.equals("byte")){
                        oout.writeObject(new AnswerObj(1,"byte"));
                        oout.flush();
                        break;
                    }else{
                        oout.writeObject(new AnswerObj(0,"success"));
                        oout.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(socketFrom.getInetAddress()+" "+ socketFrom.getLocalPort() +" end ...");
        }
    }

    public static void main(String[] args) throws IOException {
        CenterServer server = new CenterServer(8888);
        server.service();
    }
}
