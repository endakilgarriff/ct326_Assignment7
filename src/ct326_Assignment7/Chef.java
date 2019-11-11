/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread{
	private ReentrantLock re;
	private String threadName;
	private int pizzaCount = 0, burgerCount = 0, fishCount = 0;

	Chef(ReentrantLock re, String threadName) {
		this.re = re;
		this.threadName = threadName;
	}

	@Override
	public void run(){
		while (!Restaurant.orderQueue.isEmpty()) {
			boolean ans = re.tryLock();
			if(ans) {
				try {
					System.out.println("Chef " + threadName +
							" is preparing " + Restaurant.orderQueue.peek());
					ordersPrepared(Restaurant.orderQueue.peek());
					Restaurant.serverQueue.add(Restaurant.orderQueue.remove());
					Thread.sleep((long) (1625*Math.random()));
				} catch(InterruptedException e) {
						e.printStackTrace();
				} finally {
//					System.out.println("Chef " + threadName +
//							" finished making order");
					re.unlock();
				}
			}
			else {
				try {
					Thread.sleep((long) (11*Math.random()));
				}
				catch(InterruptedException e) {
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

	private String getOrdersPrepared() {
		return "Chef " + threadName + " prepared - " + pizzaCount + " Neapolitan Pizzas "
				+ burgerCount + " Cheese Burgers " + fishCount + " Fish N Chips";
	}
}
