package lectureCode;
//Future example
//Version jst@itu.dk 19/09 - 2021
//Adopted from https://github.com/eugenp/tutorials/tree/master/core-java-modules/core-java-concurrency-basic/src/main/java/com/baeldung/concurrent/future

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class futureExample{
  public static void main(String[] args){ new futureExample(); }

  public futureExample() {  
    SquareCalculator squareCalculator = new SquareCalculator();
    Future<Integer> future1 = squareCalculator.calculate(10);
    Future<Integer> future2 = squareCalculator.calculate(100);

    while (!(future1.isDone() && future2.isDone())) {
        System.out.println(
          String.format(
            "future1 is %s and future2 is %s", 
            future1.isDone() ? "done" : "not done", 
            future2.isDone() ? "done" : "not done"
          )
        );
         try { Thread.sleep(300); } catch (InterruptedException exn) { };
    }

    try { 
      Integer result1 = future1.get(); 
      Integer result2 = future2.get(); 
      System.out.println(result1 + " and " + result2);
    } catch (InterruptedException | ExecutionException e) {  e.printStackTrace();  }

    squareCalculator.shutdown();
  }
}


