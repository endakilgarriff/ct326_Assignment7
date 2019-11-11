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

	Server(ReentrantLock re, String threadName) {
		this.re = re;
		this.threadName = threadName;
	}

	@Override
	public void run() {
//		while(Restaurant.serverQueue.isEmpty()){
//			System.out.println("Waiting for orders to be ready to serve");
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		while (!finished || !Restaurant.serverQueue.isEmpty()) {
			if (!Restaurant.serverQueue.isEmpty()) {
				re.lock();
				try {
					System.out.println("Server " + threadName + " is serving " + Restaurant.serverQueue.remove());
//					re.lock();
					Thread.sleep((long) (1500*Math.random()));
				System.out.println("Lock Hold Count - " + re.getHoldCount());
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				catch(NoSuchElementException e) {
					System.out.println("No orders to serve");
				}
				finally {
					System.out.println("Server " + threadName +
							" served order");
					re.unlock();
//				System.out.println("Lock Hold Count - " +
//						re.getHoldCount());
				}
			} else {
				try {
					Thread.sleep((long) (10*Math.random()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

