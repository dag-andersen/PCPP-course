package testingconcurrency;

// JUnit testing imports
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;

// Data structures imports
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

// Concurrency imports
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class CounterTest {

    private Counter count;
    private CyclicBarrier barrier;
    private final static ExecutorService pool
	= Executors.newCachedThreadPool();

    @BeforeEach
    public void initialize() {
    	count = new CounterDR();
    	// count = new CounterSync();
    	// count = new CounterCAS();
    }


    @RepeatedTest(10)
    @Disabled
    @DisplayName("Counter Sequential")
    public void testingCounterSequential() {
	System.out.printf("Sequential counter tests with 10000 iterations");
	int localSum = 0;
	for (int i = 0; i < 10_000; i++) {
	    count.inc();
	    localSum++;
	}
	assertTrue(count.get()==localSum);
    }
    
    
    @ParameterizedTest
    // @Disabled
    @DisplayName("Counter Parallel")
    @MethodSource("argsGeneration")
    public void testingCounterParallel(int nrThreads, int N) {
	System.out.printf("Parallel counter tests with %d threads and %d iterations",
			  nrThreads,N);

	// init barrier
	barrier = new CyclicBarrier(nrThreads + 1);

	for (int i = 0; i < nrThreads; i++) {
	    pool.execute(new Turnstile(N));
	}	

	try {
	    barrier.await();
	    barrier.await();
	} catch (InterruptedException | BrokenBarrierException e) {
	    e.printStackTrace();
	}
	assertTrue(N*nrThreads == count.get(), "count.get() == "+count.get()+", but we expected "+N*nrThreads);
    }

    private static List<Arguments> argsGeneration() {

	// Max number of increments 
	final int I = 50_000;
	final int iInit = 10_000;
	final int iIncrement = 10_000;
	
	// Max exponent number of threads (2^J)
	final int J = 6;
	final int jInit = 1;
	final int jIncrement = 1;
	
	// List to add each parameters entry
	List<Arguments> list = new ArrayList<Arguments>();

	// Loop to generate each parameter entry
	// (2^j, i) for j \in {100,200,...,J} and j \in {1,..,I}
	for (int i = iInit; i <= I; i += iIncrement) {
	    for (int j = jInit; j < J; j += jIncrement) {
		list.add(Arguments.of((int) Math.pow(2,j),i));
	    }
	}

	// Return the list
	return list;
    }


    // @RepeatedTest(100_000)
    @Test
    @Disabled
    public void testingCounterParallelConstant() {
	final int N = 50;
	
	Thread t1 = new Thread() {			
	    public void run() {
		for (int i = 0; i < N; i++) {
		    count.inc();
		}
	    }
	};

	Thread t2 = new Thread() {			
	    public void run() {
		for (int i = 0; i < N; i++) {
		    count.inc();
		}
	    }
	};
	t1.start();t2.start();
	try { t1.join();t2.join(); } catch (InterruptedException e) { }   
	
	// assertTrue(N*2 == count.get(), "count.get() == "+count.get()+", but we expected "+N*2);
	// assertTrue(count.get() > 2);
    }

    /*** Test threads ***/
    public class Turnstile extends Thread {

	private final int N;

	public Turnstile(int N) { this.N = N; }
	
	public void run() {
	    try {	
		barrier.await();
		IntStream
		    .range(0,N)
		    .forEach(x -> count.inc());
		barrier.await();
	    } catch (InterruptedException | BrokenBarrierException e) {
		e.printStackTrace();
	    }
	}
    }
    
}
