package lectureCode;
//Quicksort implemented with Executors
//Version jst@itu.dk 13/09 - 2021

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.IOException;

class runProblemHeap extends Thread {  // runProblemHeap is a Task Producer
  private final ExecutorService pool;
  private int[] intArray;
  private countProblems c;  // count # of problems/task currently in the problem heap/task queue
  private int threshold;

  public runProblemHeap(int[] intArray, int poolSize, countProblems c, int threshold) {
    this.intArray= intArray;
    this.c= c;
    this.threshold= threshold;
    pool= Executors.newFixedThreadPool(poolSize);
  }

  @Override
  public void run() {
    pool.execute( new solveProblem( new Problem(intArray, 0, intArray.length-1), pool, c, threshold) );
  }
}




