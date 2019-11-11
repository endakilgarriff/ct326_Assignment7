/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ArrayBlockingQueue;
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

        ReentrantLock order = new ReentrantLock();
        ReentrantLock server = new ReentrantLock();

        Chef mark = new Chef(order, "Mark");
        Chef john = new Chef(order, "John");

        Server emily = new Server(server, "Emily");
        Server katie = new Server(server, "Katie");
        Server andrew = new Server(server, "Andrew");

        System.out.println("*******Chefs stating to prepare orders*******");

        john.start();
        mark.start();

        System.out.println("*******Servers stating to serve orders*******");

        emily.start();
        andrew.start();
        katie.start();


    }


}
