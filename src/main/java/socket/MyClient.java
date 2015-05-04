package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by adam on 2015/5/2.
 */
public class MyClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",1234);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            String msg = console.readLine();
            writer.println(msg);
            writer.flush();
            if (msg.equals("byte")) break;
//            System.out.println(reader.readLine());
        }

        socket.close();
    }
}
