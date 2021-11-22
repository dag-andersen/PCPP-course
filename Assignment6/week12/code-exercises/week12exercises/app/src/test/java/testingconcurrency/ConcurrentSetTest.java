package testingconcurrency;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.BrokenBarrierException;

public class ConcurrentSetTest {

    // Variable with set under test
    private ConcurrentIntegerSet set;
    private CyclicBarrier barrier;
    private final static ExecutorService pool = Executors.newCachedThreadPool();
    private static final int nrThreads = 8;

    // Uncomment the appropriate line below to choose the class to
    // test
    // Remember that @BeforeEach is executed before each test
    @BeforeEach
    public void initialize() {
        // init set
        // set = new ConcurrentIntegerSetBuggy();
        //set = new ConcurrentIntegerSetSync();
        set = new ConcurrentIntegerSetLibrary();
    }

    @RepeatedTest(5)
    @DisplayName("Exercise_01_Add")
    public void exercise_01_Add() {
        barrier = new CyclicBarrier(nrThreads + 1);
        var N = 2_000;

        for (int i = 0; i < nrThreads; i++) {
            pool.execute(() -> {
                try {
                    barrier.await();
                    IntStream.range(0, N).forEach(x -> set.add(x));
                    barrier.await();
                } catch (Exception e) {
                    System.out.println("failed to add");
                    e.printStackTrace();
                }
            });
        }
        try {
            barrier.await();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        assertTrue(set.size() == N, "set.size() == " + set.size() + ", but we expected " + N);
    }

    @RepeatedTest(5)
    @DisplayName("Exercise_02_Remove")
    public void exercise_02_Remove() {
        barrier = new CyclicBarrier(nrThreads + 1);
        var N = 2_000;

        for (int i = 0; i < N; i++) {
            set.add(i);
        }

        for (int i = 1; i <= nrThreads; i++) {
            pool.execute(() -> {
                try {
                    barrier.await();
                    IntStream.range(0, N).forEach(x -> set.remove(x));
                    barrier.await();
                } catch (Exception e) {
                    System.out.println("failed to remove");
                    e.printStackTrace();
                }
            });
        }
        try {
            barrier.await();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        assertTrue(set.size() == 0, "set.size() == " + set.size() + ", but we expected " + 0);
    }

    @RepeatedTest(10)
    @DisplayName("Exercise_05_SetLibrary")
    public void exercise_05_SetLibrary() {
        barrier = new CyclicBarrier(nrThreads + 1);
        var N = 10_000;

        for (int i = 1; i <= nrThreads; i++) {
            pool.execute(() -> {
                try {
                    barrier.await();
                    int size = set.size();
                    set.add(size);
                    set.remove(0);
                    barrier.await();
                } catch (Exception e) {
                    System.out.println("failed to remove");
                    e.printStackTrace();
                }
            });
        }
        try {
            barrier.await();
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        assertTrue(set.size() == 0, "set.size() == " + set.size() + ", but we expected " + 0);
    }
}
