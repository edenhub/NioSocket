package net.xdevelop.timeserver;

import java.net.Socket;
import java.io.*;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Client {
    public Client() {
    }

    public static void main(String[] args) {

//        for(int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket client = null;
                    DataOutputStream out = null;
                    DataInputStream in = null;
                    try {
                        client = new Socket("localhost", 5100);
//            client.setSoTimeout(10000);
                        out = new DataOutputStream( (client.getOutputStream()));

                        String query = "GB";
                        byte[] request = query.getBytes();
                        System.err.println("Start to write" + new Date());
                        out.write(request);
                        int cnt=0;
                        for(int i=0;i<10000;i++){
                            out.write("EN".getBytes());
                            out.flush();
                        }
                        Thread.sleep(5000);
                        System.err.println("Start to shutdown output" + new Date());
                        client.shutdownOutput();
                        Thread.sleep(5000);
                        in = new DataInputStream(client.getInputStream());
                        System.err.println("Start to read" + new Date());
                        byte[] reply = new byte[40];
                        in.read(reply);
                        System.out.println("Time: " + new String(reply, "GBK")+" "+Thread.currentThread().getName());
                        System.out.print("client closed ? "+client.isConnected());
//
//
//                        query = "GB";
//                        request = query.getBytes();
//                        out.write(request);
//                        out.flush();
//                        client.shutdownOutput();
//
//                        in = new DataInputStream(client.getInputStream());
//                        reply = new byte[40];
//                        in.read(reply);
//                        System.out.println("Second Time: " + new String(reply, "GBK")+" "+Thread.currentThread().getName());
//
//
                        in.close();
                        out.close();
                        client.close();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
            }).start();
//        }

    }

}
