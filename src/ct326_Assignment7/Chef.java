/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread {

    // Variable declarations
    private ReentrantLock chefLock;
    private ReentrantLock serverLock;
    private String threadName;
    private int pizzaCount = 0, burgerCount = 0, fishCount = 0;
    private Condition isEmpty;

    // Constructor
    Chef(ReentrantLock chefLock, ReentrantLock serverLock, String threadName, Condition isEmpty) {
        this.chefLock = chefLock;
        this.threadName = threadName;
        this.serverLock = serverLock;
        this.isEmpty = isEmpty;
    }

    @Override
    public void run() {
        // Loops while there are items in the order queue
        while (!Restaurant.getOrderQueue().isEmpty()) {
            // A chef thread acquires a lock
            chefLock.lock();
            try {
                // If the order isn't empty prepare order and send it to the queue to be served
                // Only let the servers access the server queue when there are items to serve
                if (Restaurant.getOrderQueue().peek() != null) {
                    String currentOrder = Restaurant.getOrderQueue().poll();
                    System.out.println("Chef " + threadName +
                            " is preparing " + currentOrder);
                    ordersPrepared(currentOrder);
                    serverLock.lock();
                    Restaurant.getServerQueue().add(currentOrder);
                    sleep((long) (100 * Math.random()));  // Varies how long it takes to prepare an item
                    isEmpty.signalAll();
                    serverLock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                chefLock.unlock(); // Release lock when complete preparing one item
            }
        }
        // Set finished to release servers from loop and print out how many items each thread processes
        Server.setFinished();
        System.out.println(this.getOrdersPrepared());
    }

    // Method which counts how many items each thread processes
    private void ordersPrepared(String order) {
        order = order.toLowerCase();
        if (order.contains("pizza")) {
            pizzaCount++;
        } else if (order.contains("fish n chips")) {
            fishCount++;
        } else if (order.contains("cheese burger")) {
            burgerCount++;
        } else {
            System.out.println("Unrecognised order");
        }
    }

    // Compiles string for how many items each thread processes
    private String getOrdersPrepared() {
        return "Chef " + threadName + " prepared - " + pizzaCount + " Neapolitan Pizzas "
                + burgerCount + " Cheese Burgers " + fishCount + " Fish N Chips";
    }
}
