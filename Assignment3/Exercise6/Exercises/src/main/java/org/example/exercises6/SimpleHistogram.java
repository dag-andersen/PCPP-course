package org.example.exercises6;
// For week 3

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

// sestoft@itu.dk * 2014-09-04
// thdy@itu.dk * 2019
// kasper@itu.dk * 2020

interface Histogram {
  public void increment(int bin);
  public int getCount(int bin);
  public float getPercentage(int bin);
  public int getSpan();
  public int getTotal();
}

public class SimpleHistogram {

  private static final int NO_THREADS = 10;
  private static final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NO_THREADS);
  private static CountTransactions count = new CountTransactions();

  public static void main(String[] args) {
    final Histogram histogram = new Histogram1(30);
    final int range = 4_999_999;
    final int perThread = range / NO_THREADS;
    for (int c = 1; c <= 30; c++) {
      
      final int threadCount = c;
      final int from = perThread * c, to = (c + 1 == threadCount) ? range : perThread * (c + 1);
      count.incr();
      pool.submit(new Runnable() {
        public void run() {
          for (int i = from; i < to; i++)
            if (isPrime(i))
              histogram.increment(1);

          count.decrease();
          if (count.isZero()) {
            count.finished.release();
            pool.shutdown();
          }
        }
      });
    }
    dump(histogram);
  }

  public static void dump(Histogram histogram) {
    for (int bin = 0; bin < histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
    }
    System.out.printf("      %9d%n", histogram.getTotal());
  }

  private static boolean isPrime(int n) {
    int k = 2;
    while (k * k <= n && n % k != 0)
      k++;
    return n >= 2 && k * k > n;
  }

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
          }
        }
      });
    }
    return lc.get();
  }
}

class Histogram1 implements Histogram {
  private int[] counts;
  private int total = 0;

  public Histogram1(int span) {
    this.counts = new int[span];
  }

  public void increment(int bin) {
    counts[bin] = counts[bin] + 1;
    total++;
  }

  public int getCount(int bin) {
    return counts[bin];
  }

  public float getPercentage(int bin) {
    return getCount(bin) / getTotal() * 100;
  }

  public int getSpan() {
    return counts.length;
  }

  public int getTotal() {
    return total;
  }
}

class Histogram2 implements Histogram {
  final private int[] counts;
  volatile private int total = 0;

  public Histogram2(int span) {
    this.counts = new int[span];
  }

  public synchronized void increment(int bin) {
    counts[bin] = counts[bin] + 1;
    total++;
  }

  public synchronized int getCount(int bin) {
    return counts[bin];
  }

  public float getPercentage(int bin) {
    return getCount(bin) / getTotal() * 100;
  }

  public int getSpan() {
    return counts.length;
  }

  public int getTotal() {
    return total;
  }
}
