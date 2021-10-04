package lectureCode;
//Quicksort implemented with Executors
//Basic quicksort based on code in Peter Sestofts Benchmark node
//Version jst@itu.dk 19/09 - 2021

import java.util.concurrent.Semaphore;
class countProblems{
  private int c= 1; // counting active threads + problems in heap

  synchronized void incr() { c++; }
  synchronized void decr() { c--; }
  synchronized void reset() { c= 1; }
  synchronized boolean isZero() { return c==0; }

  // The semaphore finished is used to signal to main thread that sorting is completed
  public Semaphore finished= new Semaphore(0);
}