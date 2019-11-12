/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {

    // Create Queues - One for items that the chef has to process and another for the serves to process once
    // the chefs have prepared them.
    private static ArrayBlockingQueue<String> orderQueue = new ArrayBlockingQueue<>(100, true);
    private static ArrayBlockingQueue<String> serverQueue = new ArrayBlockingQueue<>(100, true);

    // Main
    public static void main(String[] args) {

        // Read orders from file line by line and add them to Queue
        File file = new File("orderList");
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                getOrderQueue().put(sc.nextLine());
            }
            sc.close(); // Cleaning up resources
        } catch (FileNotFoundException | InterruptedException e) { // File Read errors
            e.printStackTrace();
        }

        // Create reentrant locks
        ReentrantLock order = new ReentrantLock();
        ReentrantLock server = new ReentrantLock();

        // Condition controlling when servers can access queue of prepared items
        Condition isEmpty = server.newCondition();

        // Chef threads
        Chef mark = new Chef(order, server, "Mark", isEmpty);
        Chef john = new Chef(order, server, "John", isEmpty);

        // Server threads
        Server emily = new Server(server, "Emily", isEmpty);
        Server katie = new Server(server, "Katie", isEmpty);
        Server andrew = new Server(server, "Andrew", isEmpty);

        // Start threads
        john.start();
        mark.start();

        emily.start();
        andrew.start();
        katie.start();

    }

    // Accessor methods for queues
    static ArrayBlockingQueue<String> getOrderQueue() {
        return orderQueue;
    }

    static ArrayBlockingQueue<String> getServerQueue() {
        return serverQueue;
    }

}
