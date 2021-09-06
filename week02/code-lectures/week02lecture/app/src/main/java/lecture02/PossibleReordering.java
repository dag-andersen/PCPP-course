// For week 2
// raup@itu.dk * 02/09/2021 (adapted from Goetz)
package lecture02;

public class PossibleReordering {
    // shared state for threads
    int x=0, y=0;
    int a=0, b=0;

    public PossibleReordering() throws InterruptedException {
	for (int i = 0; i < 300_000; i++) {
	    // `main` resets shared variables for each experiment
	    // Note that at this point only the `main` thread is running
	    x=0;y=0;
	    a=0;b=0;

	    // Threads definition
	    Thread one = new Thread(() -> {
		    a=1;
		    x=b;
	    });
	    Thread other = new Thread(() -> {
		    b=1;
		    y=a;
	    });

	    // We start the threads
	    // At this point, `main`, `one` and `other` are running
	    one.start();other.start();
	    // `main` waits until `one` and `other` terminate
	    // Note that `main` cannot reset the values of `x`, `y`, `a` or `b` until `one` and `other` terminate
	    one.join();other.join();
	    // `main` checks whether there has been a reordering
	    if (x==0 && y==0)
		System.out.println("("+x+","+y+")");
	}
    }

    public static void main(String[] args) throws InterruptedException {
	new PossibleReordering();
    }
}
