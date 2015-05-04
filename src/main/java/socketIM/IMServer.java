package socketIM;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by adam on 2015/5/2.
 */
public class IMServer {
    private ServerSocket server;
    private ExecutorService pools;
    private volatile boolean shutdown = false;

    public IMServer(int port){
        this(port,10);
    }

    public IMServer(int port,int poolNum){
        try {
            server = new ServerSocket(port);
            pools = Executors.newFixedThreadPool(poolNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void service() throws IOException {
        while(!shutdown){
            Socket socket = server.accept();
            System.out.println(socket.getInetAddress()+" "+socket.getPort()+" "+new Date());
            pools.execute(new IMHandler(socket));
        }
    }

    public class IMHandler implements Runnable{
        private Socket client;

        public IMHandler(Socket client){
            this.client = client;
        }

        @Override
        public void run() {
            while(true){
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    PrintWriter out = new PrintWriter(client.getOutputStream());

                    String msg = in.readLine();
                    if (msg.equals("byte")){
                        break;
                    }

                    String sentMsg = in.readLine();
                    out.println(sentMsg);
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
