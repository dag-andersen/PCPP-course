// For week 7
// raup@itu.dk * 10/10/2021
package exercises07;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntToDoubleFunction;

class TestCASLockHistogram {

    // Testing correctness and evaluating performance
    public static void main(String[] args) {

	// Create an object `histogramCAS` with your Histogram CAS implementation
	// Create an object `histogramLock` with your Histogram Lock from week 5

	// Testing correctness (uncomment lines below to test correctness)
	// countParallel(5_000_000, 10, histogramCAS);
	// dump(histogramCAS);

	// Evaluating performance of CAS vs Locks histograms Uncomment
	// snippet below to evaluate the performance both Histogram
	// implementations
	/* 
	int noThreads = 32;
	int range     = 100_000;	

	for (int i = 1; i < noThreads; i++) {
	    int threadCount = i;
	    Mark7(String.format("Count Lock histogram %2d", threadCount),
		  (j) -> {
		      countParallel(range, threadCount, histogramLock);
		      return 1.0;
		  });
	}
	
	for (int i = 1; i < noThreads; i++) {
	    int threadCount = i;
	    Mark7(String.format("Count CAS histogram %2d", threadCount),
		  (j) -> {
		      countParallel(range, threadCount, histogramCAS);
		      return 1.0;
		  });
	}
	*/
    }
    
    // Function to count the prime factors of a number `p`
    private static int countFactors(int p) {
	if (p < 2) return 0;
	int factorCount = 1, k = 2;
	while (p >= k * k) {
	    if (p % k == 0) {
		factorCount++;
		p= p/k;
	    } else 
		k= k+1;
	}
	return factorCount;
    }

    // Parallel execution of counting the number of primes for numbers in `range`
    private static void countParallel(int range, int threadCount, Histogram h) {
	final int perThread= range / threadCount;
	Thread[] threads= new Thread[threadCount];
	for (int t=0; t<threadCount; t= t+1) {
	    final int from= perThread * t, 
		to= (t+1==threadCount) ? range : perThread * (t+1); 
	    threads[t]= new Thread( () -> {
		    for (int i= from; i<to; i++) h.increment(countFactors(i));
                
	    });
	} 
	for (int t= 0; t<threadCount; t= t+1) 
	    threads[t].start();
	try {
	    for (int t= 0; t<threadCount; t= t+1) 
		threads[t].join();
	} catch (InterruptedException exn) { }
    }
    
    // Auxiliary method to print the histogram data
    public static void dump(Histogram histogram) {
	for (int bin= 0; bin < histogram.getSpan(); bin= bin+1) {
	    System.out.printf("%4d: %9d%n", bin, histogram.getCount(bin));
	}
    }

    // Benchmark function
    public static double Mark7(String msg, IntToDoubleFunction f) {
	int n = 10, count = 1, totalCount = 0;
	double dummy = 0.0, runningTime = 0.0, st = 0.0, sst = 0.0;
	do { 
	    count *= 2;
	    st = sst = 0.0;
	    for (int j=0; j<n; j++) {
		Timer t = new Timer();
		for (int i=0; i<count; i++) 
		    dummy += f.applyAsDouble(i);
		runningTime = t.check();
		double time = runningTime * 1e9 / count; 
		st += time; 
		sst += time * time;
		totalCount += count;
	    }
	} while (runningTime < 0.25 && count < Integer.MAX_VALUE/2);
	double mean = st/n, sdev = Math.sqrt((sst - mean*mean*n)/(n-1));
	System.out.printf("%-25s %15.1f ns %10.2f %10d%n", msg, mean, sdev, count);
	return dummy / totalCount;
    }   
}


/* # Exercise 7.1.2 solution

   ## Output of performance evaluation

   We observe better performance with the CAS implementation after
   running the execution with 3 threads.

   ### Output

   Count Lock histogram  1        25217209.3 ns  339113.42         16
   Count Lock histogram  2        17310888.8 ns  324452.34         16
   Count Lock histogram  3        15784347.3 ns  420654.23         16
   Count Lock histogram  4        17012682.2 ns  321620.49         16
   Count Lock histogram  5        18303556.4 ns  246310.70         16
   Count Lock histogram  6        19652979.8 ns  343585.84         16
   Count Lock histogram  7        20344986.8 ns  125863.53         16
   Count Lock histogram  8        18862058.1 ns  470680.44         16
   Count Lock histogram  9        17074388.2 ns  188893.92         16
   Count Lock histogram 10        16727388.2 ns  262301.62         16
   Count Lock histogram 11        16944838.1 ns  482991.14         16
   Count Lock histogram 12        16847659.0 ns  602529.03         16
   Count Lock histogram 13        16519522.8 ns  331458.12         16
   Count Lock histogram 14        16671652.6 ns  391052.01         16
   Count Lock histogram 15        16854988.0 ns  450434.07         16
   Count Lock histogram 16        18901399.6 ns 1108018.90         16
   Count Lock histogram 17        17021437.6 ns  243076.88         16
   Count Lock histogram 18        18010386.9 ns  739805.02         16
   Count Lock histogram 19        16743419.1 ns  319835.06         16
   Count Lock histogram 20        17069793.5 ns   86695.04         16
   Count Lock histogram 21        17149443.0 ns  328124.69         16
   Count Lock histogram 22        17329287.7 ns  624320.88         16
   Count Lock histogram 23        17677071.7 ns  399949.29         16
   Count Lock histogram 24        17108092.0 ns  105134.51         16
   Count Lock histogram 25        17868638.2 ns  560553.84         16
   Count Lock histogram 26        17669873.4 ns  130022.29         16
   Count Lock histogram 27        17679248.9 ns  358961.93         16
   Count Lock histogram 28        17244001.0 ns  319945.05         16
   Count Lock histogram 29        17089935.3 ns  121180.05         16
   Count Lock histogram 30        18301447.4 ns  202485.63         16
   Count Lock histogram 31        17388005.1 ns  658211.75         16


   Count CAS histogram  1         25942130.6 ns  587366.55         16
   Count CAS histogram  2         15626780.7 ns  163890.17         32
   Count CAS histogram  3         11195014.7 ns  107211.93         32
   Count CAS histogram  4          9732799.7 ns  613203.90         32
   Count CAS histogram  5          9925079.9 ns  257413.81         32
   Count CAS histogram  6          9287806.6 ns  187092.95         32
   Count CAS histogram  7          8613158.5 ns  186209.05         32
   Count CAS histogram  8          8188876.4 ns  396739.28         32
   Count CAS histogram  9          9745118.6 ns  343410.97         32
   Count CAS histogram 10          9567244.1 ns  414240.76         32
   Count CAS histogram 11          9113242.1 ns  269794.18         32
   Count CAS histogram 12          9618192.3 ns 1735811.58         32
   Count CAS histogram 13          9045533.2 ns  110169.90         32
   Count CAS histogram 14          8804650.8 ns  171615.10         32
   Count CAS histogram 15          8853780.5 ns 1088556.48         32
   Count CAS histogram 16          8318843.3 ns  169680.51         32
   Count CAS histogram 17          9312226.0 ns  103430.71         32
   Count CAS histogram 18          9239113.6 ns  161211.92         32
   Count CAS histogram 19          9008372.9 ns  160043.17         32
   Count CAS histogram 20          8886008.3 ns  113703.10         32
   Count CAS histogram 21          9012740.4 ns  141881.44         32
   Count CAS histogram 22          8891153.5 ns  196709.52         32
   Count CAS histogram 23          8474620.4 ns  137901.07         32
   Count CAS histogram 24          8467502.2 ns  210201.48         32
   Count CAS histogram 25          9252116.8 ns  107588.44         32
   Count CAS histogram 26          9198946.0 ns  250632.38         32
   Count CAS histogram 27          9622876.2 ns 1729567.02         32
   Count CAS histogram 28          8870050.1 ns  107503.80         32
   Count CAS histogram 29          9008635.6 ns   85051.79         32
   Count CAS histogram 30          8723123.6 ns  134286.93         32
   Count CAS histogram 31          8623605.7 ns   77550.80         32

*/

