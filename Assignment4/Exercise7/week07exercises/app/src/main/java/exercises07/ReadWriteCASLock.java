// For week 7
// raup@itu.dk * 10/10/2021
package exercises07;

class ReadWriteCASLock implements SimpleRWTryLockInterface {

    public static void main(String[] args) {
	//TODO execute tests (7.2.5 & 7.2.6)
    }

    // TODO: Add necessary field(s) for the class

    public boolean readerTryLock() {
	// TODO 7.2.3
	return true;
    }
    
    // Challenging 7.2.7: You may add new methods
	
    public void readerUnlock() {
	// TODO 7.2.4
    }
    
    public boolean writerTryLock() {
	// TODO 7.2.1
	return true;
    }

    public void writerUnlock() {
	// TODO 7.2.2
    }



    private static abstract class Holders { }

    private static class ReaderList extends Holders {
	private final Thread thread;
	private final ReaderList next;

	// TODO: Constructor

	// TODO: contains

	// TODO: remove
    }

    private static class Writer extends Holders {
	public final Thread thread;

	// TODO: Constructor
	
    }
}
