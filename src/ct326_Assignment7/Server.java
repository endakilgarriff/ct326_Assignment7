/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {

    // Variable initialization
    private ReentrantLock serverLock;
    private String threadName;
    private static boolean finished = false;
    private int pizzaCount = 0, burgerCount = 0, fishCount = 0;
    private Condition isEmpty;

    // Constructor
    Server(ReentrantLock serverLock, String threadName, Condition isEmpty) {
        this.serverLock = serverLock;
        this.threadName = threadName;
        this.isEmpty = isEmpty;
    }

    @Override
    public void run() {
        // Loops as long as chef hasn't finished processing orders
        while (!finished) {
            // Server tries to acquire lock
            serverLock.lock();
            try {
                // While there are not items for servers to serve and the chefs aren't finished.
                // Wait for the chef threads to signal that there are items on the serveQueue.
                while (Restaurant.getServerQueue().isEmpty() && !finished) {
                    try {
                        isEmpty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // While the serveQueue is not empty, process items.
                while (!Restaurant.getServerQueue().isEmpty()) {
                    String currentOrder = Restaurant.getServerQueue().poll();
                    System.out.println("Server " + threadName + " is serving " + currentOrder);
                    ordersServed(currentOrder);
                }

            } finally {
                // Release lock
                serverLock.unlock();
            }
        }
        // print out how many items each thread processes
        System.out.println(this.getOrdersServed());
    }

    // Method which counts how many items each thread processes
    private void ordersServed(String order) {
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
    private String getOrdersServed() {
        return "Server " + threadName + " prepared - " + pizzaCount + " Neapolitan Pizzas "
                + burgerCount + " Cheese Burgers " + fishCount + " Fish N Chips";
    }

    // Setter for finished boolean. Set by chef when threads finish processing orders
    static void setFinished() {
        finished = true;
    }
}

