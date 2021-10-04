
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;

class solveProblem implements Runnable {
  private Problem p;
  private ExecutorService pool;
  private countProblems c;
  private static int threshold;
    
  public solveProblem(Problem p, ExecutorService pool, countProblems c, int threshold) {
    this.p = p; 
    this.pool= pool; 
    this.c= c;
    this.threshold= threshold;
  }

  @Override
  public void run() { qsort(p, pool, c); }
  
  // Quicksort where recursive calls are replaced by submitting new task to executor
  public static void qsort(Problem problem, ExecutorService pool, countProblems c) { 
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
          SearchAndSort.swap(arr, i, j);
          i++; j--;
        }                                    
      } while (i <= j);

      if ((j-a)>= threshold) { 
        c.incr();
        pool.execute( new solveProblem( new Problem(arr, a, j), pool, c, threshold) );
      } 
      else {  SearchAndSort.qsort(arr, a, j);   }

      if ((b-i)>= threshold) {
        c.incr();
        pool.execute( new solveProblem( new Problem(arr, i, b), pool, c, threshold) );
      } else { SearchAndSort.qsort(arr, i, b); }             
    }

    c.decr(); // problem solved decrement c
    if (c.isZero()) { c.finished.release(); pool.shutdown(); }
  }
}