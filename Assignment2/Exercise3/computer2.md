# System

```
# OS:   Linux; 5.11.0-34-generic; amd64 (Ubuntu 20.04.3 LTS 64-bit)
# JVM:  Private Build; 16.0.1
# CPU:  Intel® Core™ i7-6500U CPU @ 2.50GHz × 4 "cores"
# Date: 2021-09-13T12:15:39+0200
```
# Exercise 3.1: Benchmark 
```
Mark 1:
  > 0.005 s     0.2ns 
  > 0.005 s     0.2ns 
  > 0.005 s     0.3ns

Mark 2:
  > 27.8 ns
  > 29.4 ns
  > 27.9 ns

Mark 3:
  > 27.9 ns
    27.8 ns
    28.1 ns
    27.5 ns
    27.6 ns
    27.5 ns
    27.4 ns
    27.5 ns
    27.5 ns
    27.6 ns

Mark 4:
  > 27.5 ns +/-  0.191

Mark 5:
  > 344.1 ns +/-   529.38          2
    148.2 ns +/-    69.19          4
    222.0 ns +/-    73.75          8
    160.9 ns +/-   134.50         16
     60.7 ns +/-     9.41         32
     59.2 ns +/-    12.15         64
    101.0 ns +/-   131.94        128
     57.4 ns +/-     5.13        256
     54.5 ns +/-     1.16        512
     53.4 ns +/-     1.41       1024
     53.2 ns +/-     1.53       2048
     52.6 ns +/-     1.03       4096
     32.9 ns +/-     3.08       8192
     42.5 ns +/-    33.82      16384
     31.2 ns +/-     1.19      32768
     31.9 ns +/-     3.41      65536
     28.6 ns +/-     0.56     131072
     27.8 ns +/-     0.50     262144
     27.7 ns +/-     0.35     524288
     27.6 ns +/-     0.23    1048576
     27.6 ns +/-     0.23    2097152
     27.6 ns +/-     0.18    4194304
     28.3 ns +/-     0.34    8388608
     27.5 ns +/-     0.12   16777216

Mark 6: 
  > multiply                            661.7 ns    1028.32          2
    multiply                            438.8 ns     135.59          4
    multiply                            344.5 ns     112.80          8
    multiply                            332.4 ns     177.38         16
    multiply                            181.5 ns     118.76         32
    multiply                             59.3 ns      22.91         64
    multiply                             73.5 ns      77.85        128
    multiply                             74.4 ns      13.58        256
    multiply                             72.0 ns       7.75        512
    multiply                             61.1 ns       2.01       1024
    multiply                             62.2 ns       1.31       2048
    multiply                             60.6 ns       1.33       4096
    multiply                             36.7 ns       6.25       8192
    multiply                             30.9 ns       0.65      16384
    multiply                             30.4 ns       1.10      32768
    multiply                             30.2 ns       1.88      65536
    multiply                             27.8 ns       0.40     131072
    multiply                             27.6 ns       0.37     262144
    multiply                             27.6 ns       0.34     524288
    multiply                             27.5 ns       0.18    1048576
    multiply                             27.5 ns       0.19    2097152
    multiply                             27.7 ns       0.30    4194304
    multiply                             27.7 ns       0.13    8388608
    multiply                             27.5 ns       0.06   16777216
```

# Exercise 3.2: TestTimeThreads
```
Mark 7 measurements
hashCode()                            3.0 ns       0.01  134217728
Point creation                       51.9 ns       0.23    8388608
Thread's work                      6027.0 ns      15.80      65536
Thread create                       927.4 ns       2.23     524288
Thread create start               56851.4 ns     667.49       8192
Thread create start join          79720.4 ns    2826.08       4096
ai value = 1556420000
Uncontended lock                     21.7 ns       0.07   16777216
```

# Exercise 3.4: PerfTest
```
Regular int                           1.3 ns       0.03  268435456
Volatile int                          8.8 ns       0.57   33554432
```