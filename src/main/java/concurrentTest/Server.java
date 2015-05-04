package concurrentTest;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by adam on 2015/5/3.
 */
public class Server {

    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

    public static void main(String[] args){
        Thread writer = new Writer("Wrtier");
        writer.start();

        for (int i=1;i<=10;i++){
            Thread reader = new Reader("Reader_"+i);
            reader.start();
        }
    }

    static class Writer extends Thread{

        public Writer(){
            super();
        }

        public Writer(String name){
            super(name);
        }

        @Override
        public void run() {
            int cnt=0;
            while(true){
                try {
                    Thread.sleep(1000);
                    boolean isAdd = queue.add("NewDate_"+(++cnt));
                    if (isAdd) System.out.println(Thread.currentThread().getName()+" put "+cnt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Reader extends Thread{

        public Reader(){
            super();
        }

        public Reader(String name){
            super(name);
        }
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String top = queue.poll();
                if (top!=null){
                    System.out.println(Thread.currentThread().getName()+" "+top);
                }
            }
        }
    }
}
