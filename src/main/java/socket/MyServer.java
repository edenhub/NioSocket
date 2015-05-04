package socket;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by adam on 2015/5/2.
 */
public class MyServer {

    public static void main(String[] args) throws IOException {
        System.err.println("Server start");
        ServerSocket server = new ServerSocket(8888);
        Socket socket = server.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        while(true){
            String msg = "receive : " + in.readLine();
            System.out.println(msg);
            if (msg.equals("byte")){
                break;
            }
            out.println(msg);
            out.flush();


//            while(ch != -1){
//                System.out.println(new String(buffer));
//                ch = in.read(buffer);
//            }
        }
        server.close();
        System.err.println("Server end");
    }
}
