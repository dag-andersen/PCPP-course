// For week 2
// raup@itu.dk * 02/09/2021

package lecture02;

public class NoVisibility1 {
    boolean running = true;

    public NoVisibility1() {
	new Thread(() -> {
		while (running) {
		    /* do nothing */
		}
		System.out.println("Hasta la vista, baby");
	}).start();
	// the `main` thread sleeps for 500 ms
	try{Thread.sleep(500);}catch(InterruptedException e){e.printStackTrace();}
        running = false;
	System.out.println("Main thread finishing execution");
    }

    public static void main(String[] args) {
	new NoVisibility1();
    }
}
