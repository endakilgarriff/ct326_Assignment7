/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {

    static ArrayBlockingQueue<String> orderQueue = new ArrayBlockingQueue<>(100, true);
    static ArrayBlockingQueue<String> serverQueue = new ArrayBlockingQueue<>(100, true);

    public static void main(String[] args) {

        File file = new File("orderList");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                orderQueue.put(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

        ReentrantLock order = new ReentrantLock();
        ReentrantLock server = new ReentrantLock();

        Condition isEmpty = server.newCondition();

        Chef mark = new Chef(order, server, "Mark", isEmpty);
        Chef john = new Chef(order, server, "John", isEmpty);

        Server emily = new Server(server, "Emily", isEmpty);
        Server katie = new Server(server, "Katie", isEmpty);
        Server andrew = new Server(server, "Andrew", isEmpty);

        System.out.println("*******Chefs stating to prepare orders*******");

        john.start();
        mark.start();

        System.out.println("*******Servers stating to serve orders*******");

        emily.start();
        andrew.start();
        katie.start();

    }

    public boolean checkNoOrders() {
        return orderQueue.isEmpty();
    }

}
