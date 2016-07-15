package controller;

public class Loader extends Thread implements Runnable {
	private EventHandlingController controller;

	public Loader(EventHandlingController controller) {
		this.controller = controller;
	}

	public void run() {
		while (true) {
			controller.loadShowToOverview(false);
			try {
				sleep(5000);
			} catch (InterruptedException e) {
			}
		}
	}
}
