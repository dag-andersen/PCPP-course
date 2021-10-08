package org.example.exercises6;

import java.util.concurrent.Semaphore;

public class CountTransactions {

    private int count = 0;
    
    synchronized void incr() { this.count++; }
    synchronized void decrease() { this.count--; }
    synchronized boolean isZero() { return this.count == 0; }
    public Semaphore finished = new Semaphore(0);

}