/*
    Name: Enda Kilgarriff
    Student ID: 17351606
 */

package ct326_Assignment7;

import java.util.concurrent.locks.ReentrantLock;

public class Chef extends Thread{
	private ReentrantLock re;
	private String threadName;

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
//
					System.out.println("Chef " + threadName + " is preparing " + Restaurant.orderQueue.peek());
					Restaurant.serverQueue.add(Restaurant.orderQueue.remove());
					Thread.sleep((long) (1500*Math.random()));
//					System.out.println("A Lock Hold Count - " + re.getHoldCount());
				} catch(InterruptedException e) {
						e.printStackTrace();
				} finally {
					System.out.println("Chef " + threadName +
							" finished making order");
					re.unlock();
//					System.out.println(" B Lock Hold Count - " +
//							re.getHoldCount());
				}
			}
			else {
//				System.out.println("Chef - " + threadName +
//						" waiting for order");
				try {
					Thread.sleep((long) (10*Math.random()));
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Server.finished = true;
	}

	public void ordersPrepared(String order) {
		String 
	}
}
