package org.example.exercises6;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
// Counting primes, using multiple threads for better performance.
// (Much simplified from CountprimesMany.java)
// sestoft@itu.dk * 2014-08-31, 2015-09-15
// modified rikj@itu.dk 2017-09-20
// modified jst@itu.dk 2021-09-24
import java.util.function.IntToDoubleFunction;

public class TestCountPrimesThreads {

  private static final int NO_THREADS = 10;
  private static final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NO_THREADS);
  private static CountTransactions count = new CountTransactions();

  public static void main(String[] args) {
    new TestCountPrimesThreads();
  }

  public TestCountPrimesThreads() {
    final int range = 100_000;
    Mark7("countSequential", i -> countSequential(range));
    for (int c = 1; c <= 32; c++) {
      final int threadCount = c;
      Mark7(String.format("countParallelN %2d", threadCount), i -> countParallelN(range, threadCount));
      Mark7(String.format("countParallelNLocal %2d", threadCount), i -> countParallelNLocal(range, threadCount));
      Mark7(String.format("countParallelNPool %2d", threadCount), i -> countParallelNPool(range, threadCount));
    }
  }

  private static boolean isPrime(int n) {
    int k = 2;
    while (k * k <= n && n % k != 0)
      k++;
    return n >= 2 && k * k > n;
  }

  // Sequential solution
  private static long countSequential(int range) {
    long count = 0;
    final int from = 0, to = range;
    for (int i = from; i < to; i++)
      if (isPrime(i))
        count++;
    return count;
  }

  // General parallel solution, using multiple threads
  private static long countParallelN(int range, int threadCount) {
    final int perThread = range / threadCount;
    final LongCounter lc = new LongCounter();
    Thread[] threads = new Thread[threadCount];
    for (int t = 0; t < threadCount; t++) {
      final int from = perThread * t, to = (t + 1 == threadCount) ? range : perThread * (t + 1);
      threads[t] = new Thread(() -> {
        for (int i = from; i < to; i++)
          if (isPrime(i))
            lc.increment();
      });
    }
    for (int t = 0; t < threadCount; t++)
      threads[t].start();
    try {
      for (int t = 0; t < threadCount; t++)
        threads[t].join();
      // System.out.println("Primes: "+lc.get());
    } catch (InterruptedException exn) {
    }
    return lc.get();
  }

  // General parallel solution, using multiple threads
  private static long countParallelNLocal(int range, int threadCount) {
    final int perThread = range / threadCount;
    final long[] results = new long[threadCount];
    Thread[] threads = new Thread[threadCount];
    for (int t = 0; t < threadCount; t++) {
      final int from = perThread * t, to = (t + 1 == threadCount) ? range : perThread * (t + 1);
      final int threadNo = t;
      threads[t] = new Thread(() -> {
        long count = 0;
        for (int i = from; i < to; i++)
          if (isPrime(i))
            count++;
        results[threadNo] = count;
      });
    }
    for (int t = 0; t < threadCount; t++)
      threads[t].start();
    try {
      for (int t = 0; t < threadCount; t++)
        threads[t].join();
    } catch (InterruptedException exn) {
    }
    long result = 0;
    for (int t = 0; t < threadCount; t++)
      result += results[t];
    return result;
  }

  // General parallel solution, using Thread pool
  private static long countParallelNPool(int range, int threadCount) {
    final int perThread = range / threadCount;
    final LongCounter lc = new LongCounter();
    for (int t = 0; t < threadCount; t++) {
      final int from = perThread * t, to = (t + 1 == threadCount) ? range : perThread * (t + 1);
      count.incr();
      pool.submit(new Runnable() {
        public void run() {
          for (int i = from; i < to; i++)
            if (isPrime(i))
              lc.increment();

          count.decrease();
          if (count.isZero()) {
            count.finished.release();
            pool.shutdown();

            String noTasks = "\nTotal number of tasks: " + pool.getTaskCount();
            String poolStatus = String.format("%-15s %b", "Pool isShutdown():", pool.isShutdown());
            System.out.println(noTasks + "\n" + poolStatus);
          }
        }
      });
    }
    return lc.get();
  }

  // --- Benchmarking infrastructure ---

  public static double Mark7(String msg, IntToDoubleFunction f) {
    int n = 10, count = 1, totalCount = 0;
    double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
    do {
      count *= 2;
      st = sst = 0.0;
      for (int j = 0; j < n; j++) {
        Timer t = new Timer();
        for (int i = 0; i < count; i++)
          dummy += f.applyAsDouble(i);
        runningTime = t.check();
        double time = runningTime * 1e9 / count;
        st += time;
        sst += time * time;
        totalCount += count;
      }
    } while (runningTime < 0.25 && count < Integer.MAX_VALUE / 2);
    double mean = st / n, sdev = Math.sqrt((sst - mean * mean * n) / (n - 1));
    System.out.printf("%-25s %15.1f ns %10.2f %10d%n", msg, mean, sdev, count);
    return dummy / totalCount;
  }
}
