package socketIM;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by adam on 2015/5/2.
 */
public class IM {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final String serverPort = reader.readLine();
        new Thread(new Runnable() {
            @Override
            public void run() {
                IMServer imServer = new IMServer(Integer.parseInt(serverPort));
                try {
                    imServer.service();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.err.println("IM == start == ");
        String line = reader.readLine();
        IMClient client = new IMClient(Integer.parseInt(line));
        client.ask();

        System.err.println("IM == end == ");
    }
}
