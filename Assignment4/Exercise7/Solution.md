# 7

## 7.1

### 7.1.1

> Write a class Cas Histogram so that it implements the above interface.Explain why the methods increment, getBins, getSpan() and getAndClear are thread-safe...

they use compareAndSet on atomic values.

### 7.1.2

`gradle -PmainClass=exercises07.TestCASLockHistogram run`

We don't really see any noteworthy difference... In theory the cas implementation should be faster, but the memory overhead of cas may slow it enough down to match the "slow" performance of histogramlock.

Correction:

The numbers seem wrong, since we should see a small speed increase using cas, because lock use atomic cas underneath (with more overhead).
Notice that deviation is very high for
`Count CAS histogram  1         69236767,8 ns 26359272,97         8`

## 7.2

Runs the tests:

`gradle -PmainClass=exercises07.AppTest test`
