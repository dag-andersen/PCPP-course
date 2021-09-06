// For week 2
// raup@itu.dk * 02/09/2021

package lecture02;

public class NoVisibility1Synchronized {
    boolean running = true;
    Object o = new Object();

    public NoVisibility1Synchronized() {
	new Thread(() -> {
		while (running) {
		    synchronized (o) {/* do nothing */}
		}
		System.out.println("Hasta la vista, baby");
	}).start();
	try{Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}
        synchronized (o) {running = false;}
	System.out.println("Main thread finishing execution");
    }

    public static void main(String[] args) {
	new NoVisibility1Synchronized();
    }
}
