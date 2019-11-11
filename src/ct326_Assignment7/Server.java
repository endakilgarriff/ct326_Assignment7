/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.NoSuchElementException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {

    private ReentrantLock re;
    private String threadName;
    static boolean finished = false;
    private int pizzaCount = 0, burgerCount = 0, fishCount = 0;
    Condition isEmpty;

    Server(ReentrantLock re, String threadName, Condition isEmpty) {
        this.re = re;
        this.threadName = threadName;
        this.isEmpty = isEmpty;
    }

    @Override
    public void run() {
        synchronized (isEmpty) {
            while (!finished) {
                if (!Restaurant.serverQueue.isEmpty() && re.tryLock()) {
                    try {
                        String currentOrder = Restaurant.serverQueue.take();
                        System.out.println("Server " + threadName + " is serving " + currentOrder);
                        ordersServed(currentOrder);
                        Thread.sleep((long) (100 * Math.random()));
                        System.out.println("Lock Hold Count - " + re.getHoldCount());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (NoSuchElementException e) {
                        System.out.println("No orders to serve");
                    } finally {
//					System.out.println("Server " + threadName +
//							" served order");
                        re.unlock();
//				System.out.println("Lock Hold Count - " +
//						re.getHoldCount());
                    }
                } else {
                    try {
                        Thread.sleep((long) (10 * Math.random()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(this.getOrdersServed());
        }
    }

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

    private String getOrdersServed() {
        return "Server " + threadName + " prepared - " + pizzaCount + " Neapolitan Pizzas "
                + burgerCount + " Cheese Burgers " + fishCount + " Fish N Chips";
    }
}

