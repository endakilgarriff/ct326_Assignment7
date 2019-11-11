/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReentrantLock;

public class Server extends Thread {

	private ReentrantLock re;
	private String threadName;
	static boolean finished = false;
	private int pizzaCount = 0, burgerCount = 0, fishCount = 0;

	Server(ReentrantLock re, String threadName) {
		this.re = re;
		this.threadName = threadName;
	}

	@Override
	public void run() {
		while (!finished || !Restaurant.serverQueue.isEmpty()) {
			if (!Restaurant.serverQueue.isEmpty()) {
				re.lock();
				try {
					String currentOrder = Restaurant.serverQueue.remove();
					System.out.println("Server " + threadName + " is serving " + currentOrder);
					ordersServed(currentOrder);
					Thread.sleep((long) (1235*Math.random()));
//				System.out.println("Lock Hold Count - " + re.getHoldCount());
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				catch(NoSuchElementException e) {
					System.out.println("No orders to serve");
				}
				finally {
//					System.out.println("Server " + threadName +
//							" served order");
					re.unlock();
//				System.out.println("Lock Hold Count - " +
//						re.getHoldCount());
				}
			} else {
				try {
					Thread.sleep((long) (13*Math.random()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(this.getOrdersServed());
	}

	private void ordersServed(String order) {
		order = order.toLowerCase();
		if(order.contains("pizza")){
			pizzaCount++;
		} else if (order.contains("fish n chips")){
			fishCount++;
		} else if(order.contains("cheese burger")) {
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

