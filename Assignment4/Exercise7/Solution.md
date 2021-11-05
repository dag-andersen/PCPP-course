# 7

## 7.1

### 7.1.1
> Write a class Cas Histogram so that it implements the above interface.Explain why the methods increment, getBins, getSpan() and getAndClear are thread-safe...

??

### 7.1.2

We don't really see any noteworthy difference... In theory the cas implementation should be faster, but the memory overhead of cas may slow it enough down to match the "slow" performance of histogramlock.

## 7.2

Runs the tests:

`gradle -PmainClass=exercises07.AppTest test`