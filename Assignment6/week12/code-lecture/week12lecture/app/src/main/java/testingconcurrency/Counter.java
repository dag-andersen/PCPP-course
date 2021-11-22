package testingconcurrency;

import java.util.concurrent.atomic.AtomicInteger;

public interface Counter {
    public void inc();

    public int get();
}

class CounterDR implements Counter {

    private int count;

    public CounterDR() {
        count = 0;
    }

    public void inc() {
        count++;
    }

    public int get() {
        return count;
    }
}

class CounterSync implements Counter {

    private int count;

    public CounterSync() {
        count = 0;
    }

    public synchronized void inc() {
        count++;
    }

    public int get() {
        return count;
    }
}

class CounterCAS implements Counter {

    private AtomicInteger count;

    public CounterCAS() {
        count = new AtomicInteger(0);
    }

    public void inc() {
        int currentValue;
        do {
            currentValue = count.get();
        } while (!count.compareAndSet(currentValue, currentValue + 1));
    }

    public int get() {
        return count.get();
    }
}
