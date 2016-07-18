package controller;

/**
 * @author Tim Meier & Sandro Guerotto
 * @date 17.07.2016
 * @version 10.00
 * @program CineBook
 * @function Ein Dauerschleife Thread welcher die Show Anzeige aktulisiert
 */

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
