package testingconcurrency;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import org.junit.jupiter.api.Assertions.*;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicInteger;


public class BoundedBufferTest {

    private BoundedBuffer<Integer> buffer;
    private CyclicBarrier barrier;
    private final static ExecutorService pool
	= Executors.newCachedThreadPool();
    private AtomicInteger putSum;
    private AtomicInteger takeSum;
    

    @BeforeEach
    public void initialize() {
	putSum  = new AtomicInteger(0);
	takeSum = new AtomicInteger(0);
    }

    @ParameterizedTest
    @DisplayName("PutTakeTest of Bounded Buffer")
    @MethodSource("argsProvider")    
    public void putTakeTest(int nrThreads,
			    int nrTrials,
			    int bufferSize) {
	System.out.printf("Running test with parameters: (%d,%d,%d)",
			  nrThreads, nrTrials, bufferSize);

	// init buffer
	// buffer  = new BoundedBufferMonitor<Integer>(bufferSize);
	buffer  = new BoundedBufferSemaphore<Integer>(bufferSize);
	
	// init barrier
	barrier = new CyclicBarrier((nrThreads*2) + 1);

	for (int i = 0; i < nrThreads; i++) {
	    pool.execute(new Producer(nrTrials));
	    pool.execute(new Consumer(nrTrials));
	}	

	try {
	    barrier.await();
	    barrier.await();
	} catch (InterruptedException | BrokenBarrierException e) {
	    e.printStackTrace();
	}
	assert(putSum.get() == takeSum.get());
    }    

    private static List<Arguments> argsProvider() {

	// Max number of trials
	final int I = 200;
	final int iInit = 50;
	final int iIncrement = 50;
	
	// Max exponent number of threads (2^J)
	final int J = 5;
	final int jInit = 1;
	final int jIncrement = 1;

	
	// Max number of buffer size
	final int K = 100;
	final int kInit = 20;
	final int kIncrement = 20;
	
	// List to add each parameters entry
	List<Arguments> list = new ArrayList<Arguments>();

	// Loop to generate each parameter entry
	// (2^j, i, k) for i \in {50,100,...,J} and j \in {1,..,I} and k \in {20,40,...,K}
	for (int i = iInit; i <= I; i += iIncrement) {
	    for (int j = jInit; j < J; j += jIncrement) {
		for (int k = kInit; k < K; k += kIncrement) {
		    list.add(Arguments.of((int) Math.pow(2,j), i, k));
		}		
	    }
	}

	// Return the list
	return list;
    }


    


    /**** Threads to test ****/
    class Producer extends Thread {
	int nrTrials;
	int localSum;

	public Producer(int nrTrials) {
	    this.nrTrials = nrTrials;
	    this.localSum = 0;
	}
	
	public void run() {
	    try {
		barrier.await();
		for (int i = 0; i < nrTrials; i++) {
		    Random r  = new Random();
		    int toPut = r.nextInt();
		    buffer.put(toPut);
		    localSum += toPut;		    
		}
		putSum.addAndGet(localSum);
		barrier.await();	
	    } catch (InterruptedException | BrokenBarrierException e) {
		e.printStackTrace();
	    }
	}
    }

    class Consumer extends Thread {
	int nrTrials;
	int localSum;

	public Consumer(int nrTrials) {
	    this.nrTrials = nrTrials;
	    this.localSum = 0;
	}
	
	public void run() {
	    try {
		barrier.await();
		for (int i = 0; i < nrTrials; i++) {
		    localSum += buffer.take();
		}
		takeSum.addAndGet(localSum);
		barrier.await();		
	    } catch (InterruptedException | BrokenBarrierException e) {
		e.printStackTrace();
	    }
	}
    }

    
}
