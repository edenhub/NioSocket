package socket2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adam on 2015/5/2.
 */
public class MyServer {
    private ExecutorService pool;
    private ServerSocket server;
    private volatile boolean shutdown = false;

    public MyServer(int port){
        try {
            server = new ServerSocket(port);
            pool = Executors.newFixedThreadPool(10);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void service() throws IOException {
        while(!shutdown){
            Socket socket = server.accept();
            System.err.println("Log : receive from "+socket.getInetAddress().toString());
            pool.execute(new Handler(socket));
        }
    }

    public void shutDown(){
        this.shutdown = true;
    }

    public class Handler implements Runnable{
        private Socket socketFrom;
//        private Socket socketTo;
        public Handler(Socket socket){
            this.socketFrom = socket;
//            try {
//                socketTo = new Socket(socketFrom.getInetAddress(),socketFrom.getPort());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void run() {
            BufferedReader readerFrom = null;
            BufferedReader readerTo = null;

            PrintWriter outFrom = null;
            PrintWriter outTo = null;
            try {
                readerFrom = new BufferedReader(new InputStreamReader(socketFrom.getInputStream()));
                readerTo = new BufferedReader(new InputStreamReader(System.in));

                outFrom = new PrintWriter(socketFrom.getOutputStream());
//                outTo = new PrintWriter(socketTo.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            while(true){
                try {
                    String recMsg = readerFrom.readLine();
                    System.err.println(new Date()+" receive from "+socketFrom.getInetAddress()+" "+recMsg);
                    if (recMsg.equals("byte")) break;
                    String sendMsg = readerTo.readLine();
                    outFrom.write(sendMsg);
                    outFrom.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socketFrom.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            try {
                socketFrom.close();
//                socketTo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    public static void main(String[] args) throws IOException {
        System.err.println("Service start ...");
        MyServer myServer = new MyServer(8888);
        myServer.service();
    }
}
