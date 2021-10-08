package org.example.exercises6;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadsAccountExperimentsMany {

  static final int N = 10;
  static final int NO_TRANSACTION=5;
  static final int NO_THREADS = 10;
  static final Account[] accounts = new Account[N];
  static final Thread[] threads = new Thread[NO_THREADS];
  static Random rnd = new Random();


  // Exercise 6.1.3
  private static final ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(NO_THREADS);

  // Exercise 6.1.4
  private static CountTransactions c = new CountTransactions();

  public static void main(String[] args){ new ThreadsAccountExperimentsMany(); }

  public ThreadsAccountExperimentsMany(){
    for( int i = 0; i < N; i++){
      accounts[i] = new Account(i);
    }

    // Exercise 6.1.3
    for (int i = 0; i < N; i++) {
      doNTransactions(NO_TRANSACTION);  
    } 

  //  for( int i = 0; i<NO_THREADS; i++){
  //    try{ (threads[i] = new Thread( () -> doNTransactions(NO_TRANSACTION) )).start();}
  //    catch(Error ex){
  //      System.out.println("At i = " + i + " I got error: " + ex);
  //      System.exit(0);
  //    }
  //  }
  //  for( int i = 0; i<NO_THREADS; i++){
  //    try {threads[i].join();} catch(Exception dummy){};
  //  }
  }

  private static void doNTransactions(int noTransactions){
    for(int i = 0; i<noTransactions; i++){
      long amount = rnd.nextInt(5000)+100;
      int source = rnd.nextInt(N);
      int target = (source + rnd.nextInt(N-2)+1) % N; // make sure target <> source
      
      // Exercise 6.1.3
      Runnable task = new Runnable() {
        public void run() {
          doTransaction( new Transaction( amount, accounts[source], accounts[target]));
        }
      };

      // Exercise 6.1.4
      c.incr();
      
      pool.execute(task);
    }
  }

  private static void doTransaction(Transaction t){
    // Exercise 6.1.3
    String poolActiveCount = String.format("%-15s %3d", "PoolActiveCount:", pool.getActiveCount());
    System.out.println(t + poolActiveCount);

    t.transfer();

    // Exercise 6.1.4
    c.decrease();
    if (c.isZero()) {
      c.finished.release();
      pool.shutdown();
      
      String noTasks = "\nTotal number of tasks: " + pool.getTaskCount();
      String poolStatus = String.format("%-15s %b", "Pool isShutdown():", pool.isShutdown());
      System.out.println(noTasks + "\n" + poolStatus);
    }
  }

  static class Transaction {
    final Account source, target;
    final long amount;
    Transaction(long amount, Account source, Account target){
      this.amount = amount;
      this.source = source;
      this.target = target;
    }

    public void transfer(){
      
      Account min = accounts[Math.min(source.id, target.id)];
      Account max = accounts[Math.max(source.id, target.id)];
      synchronized(min){
        synchronized(max){
    //      Exercise 6.1.2      
    //      Account s = accounts[source.id];
    //      Account t = accounts[target.id];
    //      synchronized (s) {
    //        synchronized (t) {
          source.withdraw(amount);
          try{Thread.sleep(50);} catch(Exception e){}; // Simulate transaction time
          target.deposit(amount);
        }
      }
    }

    public String toString(){
      return String.format("%-30s", "Transfer: " + String.format("%5d", amount)  + " from " + source.id + " to " + target.id);
    }
  }

  static class Account{
    // should have transaction history, owners, account-type, and 100 other real things
    public final int id;
    private long balance = 0;
    Account( int id ){ this.id = id;}
    public void deposit(long sum){ balance += sum; }
    public void withdraw(long sum){ balance -= sum; }
    public long getBalance(){ return balance; }
  }

}
