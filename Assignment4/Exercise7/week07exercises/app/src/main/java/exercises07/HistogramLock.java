package exercises07;

public class HistogramLock implements Histogram {
  final private int[] bins;
  volatile private int total = 0;

  public HistogramLock(int span) {
    this.bins = new int[span];
  }

  public synchronized void increment(int bin) {
    bins[bin] = bins[bin] + 1;
    total++;
  }

  public synchronized int getCount(int bin) {
    return bins[bin];
  }

  public float getPercentage(int bin) {
    return getCount(bin) / getTotal() * 100;
  }

  public int getSpan() {
    return bins.length;
  }

  public int getTotal() {
    return total;
  }

  public synchronized int getAndClear(int bin) {
    int val = bins[bin];
    bins[bin] = 0;
    return val;
  }
}
