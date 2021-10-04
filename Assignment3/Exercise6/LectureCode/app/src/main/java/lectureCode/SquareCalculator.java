package lectureCode;
//Future example
//Version jst@itu.dk 19/09 - 2021
//Adopted from https://github.com/eugenp/tutorials/tree/master/core-java-modules/core-java-concurrency-basic/src/main/java/com/baeldung/concurrent/future

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SquareCalculator {    
    
    private ExecutorService executor 
      = Executors.newSingleThreadExecutor();
    
    public Future<Integer> calculate(Integer input) {        
        return executor.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }

    public void shutdown(){ executor.shutdown(); }
}