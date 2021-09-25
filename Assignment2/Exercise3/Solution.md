# Exercises week 3

## Exercise 3.1

Command: `gradle -PmainClass=exercises03.Benchmark run`

Looking at the output results in [computer2.md](computer2.md). We see that the results are very similar to the ones in the Benchmark note. This is properly because the machines used are close in regards to hardware. 

- Mark 1 
    - Just like seen in the Benchmark note, the results are highly implausible properly due to the JIT compiler, and are therefore useless. 
- Mark 2
    - Again very close to the Benchmark note only a little bit faster. Reasonable but still a bit too slow compared to the estimated result 8-20ns.
- Mark 3
    - All the iterations are consistent with the results from the Mark 2.
- Mark 4
    - Again very consistent with the previous results. The variation is very small, which indicates that the results are somewhat reliable.
- Mark 5
    - The results matches the tendency of those in the Benchmark note, where the standard deviation becomes smaller as the number of iterations(counts) grows, and the last result is consistent with that of the *mark 4*. We do see two sudden increases in the mean for count 128 and 16384. The standard deviation is however pretty high in both these cases, so we discard them since we cannot hold any confidence in them.
- Mark 6
    - We don't see anything unsuspected. The result matches the ones from the Benchmark note and is consistent with out previous results as well. Running the *Mark 6* several times we see that an increase in the mean and a high standard deviation always occurs on *count* 128. Why this happens is unknown, but we suspect that it is hardware related, since it seems unlikely that the garbage collector or a random process always skewers the result on that *count*.

## Exercise 3.2

Command: `gradle -PmainClass=exercises03.Benchmark run`

Running *TestTimeThreads* multiple times with *Mark 6*, show us that testing the threads has a much higher standard deviation than the other functions. While the standard deviation and the mean still decrease with the increase in counts, the results of *Thread create start* and *Thread create start join* are still incredibly high compared to the other function. 

Changing to *Mark 7* gave us the expected result ([computer2.md](computer2.md)) compared to the multiple runs with *Mark 6*. The results were consistent with no surprises. 

## Exercise 3.3

Command: `gradle -PmainClass=exercises03.TestCountPrimesThreads run`

### 3.3.2
No real surprises. The performance doesn't increase after 2 threads, since my computer only has 2 cores. 


### 3.3.3
Discuss the results: is the performance of AtomicLong better or worse than that of LongCounter? 
> Same performance. 

Should one in general use adequate built-in classes and methods when they exist?
> yes, because don't implement something that already exists.


## Exercise 3.4

Command: `gradle -PmainClass=exercises03.Benchmark run`

As expected the results ([computer2.md](computer2.md)) show that incrementing the volatile int takes longer compared to the regular int. This was expected since the regular int is stored in the CPU cache where the volatile int is stored in main memory.