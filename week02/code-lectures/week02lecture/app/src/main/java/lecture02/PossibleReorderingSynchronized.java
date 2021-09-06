// For week 2
// raup@itu.dk * 02/09/2021 (adapted from Goetz)
package lecture02;

public class PossibleReorderingSynchronized {
    int x=0, y=0;
    int a=0, b=0;
    Object o = new Object();

    public PossibleReorderingSynchronized() throws InterruptedException {
	for (int i = 0; i < 1_000_000; i++) {
	    x=0;y=0;
	    a=0;b=0;

	    // Threads definition
	    Thread one = new Thread(() -> {
		    synchronized (o) {
			a=1;
			x=b;
		    }
	    });
	    Thread other = new Thread(() -> {
		    synchronized (o) {
			b=1;
			y=a;
		    }
	    });
	    one.start();other.start();
	    one.join();other.join();
	    // This condition never holds
	    if (x==0 && y==0)
		System.out.println("("+x+","+y+")");
	}
    }

    public static void main(String[] args) throws InterruptedException {
	new PossibleReorderingSynchronized();
    }
}
