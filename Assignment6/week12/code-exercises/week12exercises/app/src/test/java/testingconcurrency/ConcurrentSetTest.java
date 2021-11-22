package testingconcurrency;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        set = new ConcurrentIntegerSetBuggy();
        // set = new ConcurrentIntegerSetSync();
        // set = new ConcurrentIntegerSetLibrary();
    }

    @RepeatedTest(10)
    @DisplayName("Exercise_01_Add")
    public void exercise_01_Add() {
        barrier = new CyclicBarrier(nrThreads + 1);
        var N = 3;

        for (int i = 0; i < nrThreads; i++) {
            pool.execute(() -> {
                try {
                    barrier.await();
                    for (int j = 0; j < N; j++) {
                        int size = set.size();
                        set.add(size);
                    }
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

        assertTrue(N * nrThreads == set.size(), "set.size() == " + set.size() + ", but we expected " + N * nrThreads);
    }
}
