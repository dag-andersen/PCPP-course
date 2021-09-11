package exercises01;

import java.util.concurrent.locks.ReentrantLock;

public class Printer {

    ReentrantLock lock = new ReentrantLock();

    public void print() {
        lock.lock();
        System.out.print("-");
        try {
            Thread.sleep(50);
        } catch (InterruptedException exn) {
        }
        System.out.print("|");
        lock.unlock();
    }
}