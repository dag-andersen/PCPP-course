package exercises07;

public class HistogramLock {
    final private int[] counts;
    volatile private int total = 0;
  
    public HistogramLock(int span) {
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
