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

  // Exercise 6.3.2
  static final int N = 30;
  private static final int NO_THREADS = 10;
  private static final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NO_THREADS);
  private static CountTransactions counter = new CountTransactions();

  public static void main(String[] args) {
    final Histogram histogram = new Histogram2(N);
    final int range = 4_999_999;

    for (int i = 0; i <= range; i++) {

      int p = i;

      Runnable task = new Runnable() {
        public void run() {
          int noPrimeFactors = primeFactors(p);
          histogram.increment(noPrimeFactors);

          counter.decrease();

          if (counter.isZero()) {
            counter.finished.release();
            pool.shutdown();
            dump(histogram);
          }
        }
      };

      counter.incr();
      pool.execute(task);

    }
  }

  // Exercise 6.3.2
  public static int primeFactors(int n) {
    if (n == 0)
      return 0;

    int count = 0;

    // count the number of 2s that divide n
    while (n % 2 == 0) {
      count++;
      n /= 2;
    }

    // n must be odd at this point. So we can
    // skip one element (Note i = i + 2)
    for (int i = 3; i <= Math.sqrt(n); i += 2) {
      // While i divides n, count i and divide n
      while (n % i == 0) {
        count++;
        n /= i;
      }
    }

    // This condition is to handle the case when
    // n is a prime number greater than 2
    if (n > 2)
      count++;

    return count;
  }

  public static void dump(Histogram histogram) {
    for (int bin = 0; bin < histogram.getSpan(); bin++) {
      System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
    }
    System.out.printf("      %9d%n", histogram.getTotal());
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

// Exercise 6.3.1
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
