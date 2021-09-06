// For week 2
// raup@itu.dk * 02/09/2021 (adapted from Goetz)
package lecture02;

public class PossibleReorderingVolatile {
    int x=0;
    int y=0;
    volatile int a=0;
    volatile int b=0;

    public PossibleReorderingVolatile() throws InterruptedException {
	for (int i = 0; i < 3_000_000; i++) {
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
	    one.start();other.start();
	    one.join();other.join();
	    // This condition never holds
	    if (x==0 && y==0)
		System.out.println("("+x+","+y+")");
	}
    }

    public static void main(String[] args) throws InterruptedException {
	new PossibleReorderingVolatile();
    }
}
