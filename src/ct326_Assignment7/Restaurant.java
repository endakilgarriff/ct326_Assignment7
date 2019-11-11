/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {

    static ArrayBlockingQueue<String> orderQueue = new ArrayBlockingQueue<>(100,true);
    static ArrayBlockingQueue<String> serverQueue = new ArrayBlockingQueue<>(100,true);

    public static void main(String[] args) {


        File file = new File("C:/TextFiles/orderList");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                orderQueue.add(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("*******Printing orderList*******");
        // Adding orders to queue
        for (String i : orderQueue) {
            System.out.println(i);
        }
        ReentrantLock order = new ReentrantLock();
        ReentrantLock server = new ReentrantLock();

        Chef mark = new Chef(order, "Mark");
        Chef john = new Chef(order, "John");

        Server emily = new Server(server, "Emily");
        Server katie = new Server(server, "Katie");
        Server andrew = new Server(server, "Andrew");

        System.out.println("*******Chefs stating to prepare orders*******");

//        john.start();
//        mark.start();
//
//        emily.start();
//        andrew.start();
//        katie.start();


        ExecutorService chefs = Executors.newFixedThreadPool(2);
        ExecutorService servers = Executors.newFixedThreadPool(3);
        chefs.execute(mark);
        chefs.execute(john);
        servers.execute(katie);
        servers.execute(emily);
        servers.execute(andrew);
        chefs.shutdown();
        servers.shutdown();


//        System.out.println("*******Printing serverQueue*******");
//
//
//        for(String i: serverQueue) {
//            System.out.println(i);
//        }
    }
}
