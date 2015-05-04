package socketIM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by adam on 2015/5/2.
 */
public class IMClient {
    private Socket socket;

    public IMClient(int port){
        this("localhost",port);
    }

    public IMClient(String addr,int port){
        try {
            socket = new Socket(addr,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ask() throws IOException {
        while(true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            String msg = reader.readLine();
            if (msg.equals("byte")){
                out.println(msg);
                out.flush();
                break;
            }else{
                out.println(msg);
                out.flush();
                String receive = in.readLine();
                System.out.println("Rec : "+receive);
            }
        }

        System.err.println("Client shut down");
    }
}
