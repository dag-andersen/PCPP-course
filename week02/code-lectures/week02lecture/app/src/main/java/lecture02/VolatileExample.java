// For week 2
// raup@itu.dk * 05/09/2021 (adapted from Goetz & Manson: http://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html#volatile)
package lecture02;

class VolatileExample {

    public VolatileExample() {
	VolatileReaderWriter vrw = new VolatileReaderWriter();

	for (int i = 0; i < 1_000_000; i++) {
	    new Thread(() -> {
		    vrw.reader();
	    }).start();

	    new Thread(() -> {
		    vrw.writer();
	    }).start();	    
	}
    }

    public static void main(String[] args) {
	new VolatileExample();
    }

    class VolatileReaderWriter {
	
	int x = 0;
	volatile boolean v = false;

	public void writer() {
	    x = 42;
	    v = true;
	}
	
	public void reader() {
	    if (v == true)
		System.out.println(x); // this if-branch is guaranteed to see 42
	    else
		System.out.println(x);
	}
    }   

}
