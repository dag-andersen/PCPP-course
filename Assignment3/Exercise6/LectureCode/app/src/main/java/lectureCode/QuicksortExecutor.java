package lectureCode;
//QuickSort using Java Executor to simulate Problem Heap
//Version jst@itu.dk 19/09 - 2021

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;

public class QuicksortExecutor{
  private static final int maxThreads = 33;
  private static final int pSize= 1_000_000; // size of array to be sorted
  private static final int threshold= 10000; // problems with size smaller not further subdivided
  
  public static void main(String[] args){ new QuicksortExecutor(); }

  public QuicksortExecutor() {
    for (int n= 1; n < maxThreads; n= 2*n)  runSize(n, 1_000_000, threshold);
  }

	private static void testSorted(int[] a) {
	 int c= 0;
	 while ( c < (a.length-1) ) 
		if (a[c] <= a[c+1]) c= c+1;
		else { System.out.println("Error at "+c); break; }
	 //a is ordered
	 if (c == a.length-1) System.out.println("Success!");
	}

  private static void setUpQS(int threadCount, int[] intArray, countProblems c, int threshold) {
    new runProblemHeap(intArray, threadCount, c, threshold).start();
  }

  private static void finishQS(countProblems c) { 
    try { c.finished.acquire(); }
    catch (java.lang.InterruptedException e) { System.out.println(e.toString()); }
  }

  private static void runSize(int threadCount, int pSize, int threshold) {
    final int[] intArray= SearchAndSort.fillIntArray(pSize);
    final countProblems c= new countProblems();

    Benchmark.Mark8Setup("Executor Quicksort", 
          String.format("%2d", threadCount),
          new Benchmarkable() { 
            public void setup() {
              SearchAndSort.shuffle(intArray);
              c.reset();  // c==1     
              setUpQS(threadCount, intArray, c, threshold);
            }
            public double applyAsDouble(int i) {
              finishQS(c); 
              //testSorted(intArray);
              return 0.0;
            }
          }
    );
  }
}

  