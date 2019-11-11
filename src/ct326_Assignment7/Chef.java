/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread {
    private ReentrantLock chefLock;
    private ReentrantLock serverLock;
    private String threadName;
    private int pizzaCount = 0, burgerCount = 0, fishCount = 0;
    Condition isEmpty;

    Chef(ReentrantLock chefLock, ReentrantLock serverLock, String threadName, Condition isEmpty) {
        this.chefLock = chefLock;
        this.threadName = threadName;
        this.isEmpty = isEmpty;
        this.serverLock = serverLock;
    }

    @Override
    public void run() {
        while (!Restaurant.orderQueue.isEmpty()) {
            if (chefLock.tryLock()) {
                try {
                    String currentOrder = Restaurant.orderQueue.take();
                    System.out.println("Chef " + threadName +
                            " is preparing " + currentOrder);
                    ordersPrepared(currentOrder);
                    Restaurant.serverQueue.add(currentOrder);
                    sleep((long) (100 * Math.random()));
                    isEmpty.signalAll();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
//					System.out.println("Chef " + threadName +
//							" finished making order");
                    chefLock.unlock();
                }
            } else {
                try {
                    Thread.sleep((long) (10 * Math.random()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Server.finished = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.getOrdersPrepared());
    }

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

    private String getOrdersPrepared() {
        return "Chef " + threadName + " prepared - " + pizzaCount + " Neapolitan Pizzas "
                + burgerCount + " Cheese Burgers " + fishCount + " Fish N Chips";
    }
}
