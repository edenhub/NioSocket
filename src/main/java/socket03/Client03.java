package socket03;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by adam on 2015/5/2.
 */
public class Client03 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost",8888);

        ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            String action = console.readLine();
            if (action.equals("1")){
                UserObj user = new UserObj(socket.getLocalAddress().toString()+" "+socket.getLocalPort(),new Date().toString(),"hello");
                oout.writeObject(user);
                oout.flush();
            }else{
                UserObj user = new UserObj("byte",new Date().toString(),"byte");
                oout.writeObject(user);
                oout.flush();
                break;
            }

            AnswerObj answerObj = (AnswerObj) oin.readObject();
            System.out.println(answerObj);
        }

        System.err.println("===end===");
    }
}
