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
    private Condition isEmpty;

    Chef(ReentrantLock chefLock, ReentrantLock serverLock, String threadName, Condition isEmpty) {
        this.chefLock = chefLock;
        this.threadName = threadName;
        this.serverLock = serverLock;
        this.isEmpty = isEmpty;
    }

    @Override
    public void run() {
        while (!Restaurant.orderQueue.isEmpty()) {
            chefLock.lock();
                try {
                    if(Restaurant.orderQueue.peek() != null){
                        String currentOrder = Restaurant.orderQueue.poll();
                        System.out.println("Chef " + threadName +
                                " is preparing " + currentOrder);
                        ordersPrepared(currentOrder);
                        serverLock.lock();
                        Restaurant.serverQueue.add(currentOrder);
                        sleep((long) (100 * Math.random()));
                        isEmpty.signalAll();
                        serverLock.unlock();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    chefLock.unlock();
                }
        }
        Server.finished = true;
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
