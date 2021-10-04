
import java.util.*;
import java.util.concurrent.TimeUnit;
public class ProblemHeapSortingBenchmarkable{
  private static int threshold = 100; 
    // problems smaller than threshold are not subdivided
    // this could be a parameter to quicksort, but the overhead is significant
  private static int maxThreads = 65;
  static final Thread[] threads = new Thread[maxThreads];

	public static void main(String[] args){ new ProblemHeapSortingBenchmarkable(); }

  public ProblemHeapSortingBenchmarkable() {
    //for (int n= 1; n < maxThreads; n= 2*n) {
      runSize(4, 100_000);
    //}
	}

  public static void problemHeapStart(int threadCount, int pSize, int[] intArray) {
    ProblemHeap heap= new ProblemHeap(1);
	  heap.add(new Problem(intArray, 0, pSize-1));
		for (int t=0; t<threadCount; t++) { 
			threads[t]= new Thread( () -> { 
				try {
					Problem newProblem= heap.getProblem(); 
					while (newProblem != null) {  // when newProblem == null alg stops
						qsort(newProblem, heap);
						newProblem= heap.getProblem(); 
					} 
				}catch (InterruptedException exn) { System.out.println("InterruptedException");}
			});
    }
  }

  public static void problemHeapFinish(int threadCount, int[] intArray){
    for (int t=0; t<threadCount; t++)  threads[t].start();
		try {
		  for (int t=0; t<threadCount; t++) 
			threads[t].join();
		} catch (InterruptedException exn) { System.out.println("InterruptedException");}
    //testSorted(intArray); //only needed while testing
  }

	private static void swap(int[] arr, int s, int t) {
		int tmp = arr[s];  arr[s] = arr[t];  arr[t] = tmp;
	}	

	// Quicksort
	private static void qsort(Problem problem, ProblemHeap heap) { 
    // modified Quicksort using a Problem heap
    int[] arr= problem.arr;
    int a= problem.low;
    int b= problem.high;

		if (a < b) { 
		  int i = a, j = b;
		  int x = arr[(i+j) / 2];                
		  do {                                   
			while (arr[i] < x) i++;              
			while (arr[j] > x) j--; 
			if (i <= j) {
			  swap(arr, i, j);
			  i++; j--;
			}                                    
		  } while (i <= j);
		  if ((j-a)>= threshold) heap.add(new Problem(arr, a, j)); else qsort(new Problem(arr, a, j), heap);
		  if ((b-i)>= threshold) heap.add(new Problem(arr, i, b)); else qsort(new Problem(arr, i, b), heap);                
		}                                        
	}

	public static void testSorted(int[] a) {
    int c= 0;
    while ( c < (a.length-1) )
      if (a[c] <= a[c+1]) c= c+1;
      else {System.out.println("Error at "+c); break; }
      //a is ordered
      if (c == a.length-1) System.out.println("Success!"); 
  }
	
  private static void runSize(int threadCount, int pSize) {
    final int[] intArray = SearchAndSort.fillIntArray(pSize);
    Benchmark.Mark8Setup("Problem heap quicksort", 
          String.format("%2d", threadCount),
          new Benchmarkable() { 
            public void setup() {
              SearchAndSort.shuffle(intArray); 
              problemHeapStart(threadCount, pSize, intArray);
            }
            public double applyAsDouble(int i) {
              problemHeapFinish(threadCount, intArray); return 0.0;
            }
          }
    );
  }

}

  