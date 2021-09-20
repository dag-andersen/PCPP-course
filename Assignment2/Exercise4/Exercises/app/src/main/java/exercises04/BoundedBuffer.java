package exercises04;

import java.util.LinkedList;
import java.util.concurrent.*;

public class BoundedBuffer<T> implements BoundedBufferInteface<T> {

    final LinkedList<T> queue;
    final Semaphore semaphore;
    final Semaphore semaphoreLock;

    public BoundedBuffer(int capacity) {
        queue = new LinkedList<T>();
        semaphore = new Semaphore(capacity);
        semaphoreLock = new Semaphore(1);
    }

    @Override
    public T take() throws Exception {
        semaphoreLock.acquire();
        var item = queue.poll();
        semaphoreLock.release();
        semaphore.release();
        return item;
    }

    @Override
    public void insert(T elem) throws Exception {
        semaphore.acquire();
        semaphoreLock.acquire();
        queue.add(elem);
        semaphoreLock.release();
    }

    public static void main(String[] args) {
        try {
            BoundedBuffer<Integer> buffer = new BoundedBuffer<Integer>(5);
            for (int i = 0; i < 5; i++) {
                System.out.println("Inserting " + i);
                buffer.insert(i);
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(buffer.take());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
