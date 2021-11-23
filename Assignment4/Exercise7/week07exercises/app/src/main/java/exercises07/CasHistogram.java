package exercises07;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

// Lock free Histogram implementation
public class CasHistogram implements Histogram {

    private final AtomicReference<Thread> holder = new AtomicReference<Thread>();

    // TA Question: Should this be an AtomicReference<AtomicInteger[]> or not?
    private final AtomicInteger[] buckets;

    public CasHistogram(int n) {
        buckets = new AtomicInteger[n];
    }

    public void increment(int bin) {
        while (true) {
            int oldValue = buckets[bin].get();
            int newValue = oldValue + 1;
            if (buckets[bin].compareAndSet(oldValue, newValue)) {
                return;
            }
        }
    }

    public int getCount(int bin) {
        return buckets[bin].get();
    }
    

    public int getSpan() {
        return buckets.length;
    }

    public int getAndClear(int bin) {
        while (true) {
            int oldValue = buckets[bin].get();
            int newValue = 0;
            if (buckets[bin].compareAndSet(oldValue, newValue)) {
                return oldValue;
            }
        }
    }
}
