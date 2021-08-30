# Exercise 1.1

## 1
Output: ```Count is 19779348 and should be 20000000```

Answer: ```The output is less than the expected 20 mio.```

## 2
Output: ```Count is 200 and should be 200```

Explain how this could be: ```The count is so small that one thread completes all iterations before the other thread starts.```

Would you say that it is guaranteed that the output is always 200: ```No``` 

## 3
Output: ```Count is 200 and should be 200```

Answer: ```No, it doesn't make a difference. why? 'count++' is syntax sugar and it's decompiled to more than one operation and therefore *race condition* will still happen.```

## 4

Question:
> Explain why your solution is correct, and why no other output is possible.

Code:
```
lock.lock();
count++;
lock.unlock();
```

Notes: ```
count++ is the critical section.
There is no race-condition
There is mutual-exclusion
The number of interleavings are finite.
It can't deadlock.
```

Answer:
```
Lets define "correct": The program always prints 20_000.
The program is correct since the critical section can only be accessed by one thread at the time (mutual exclusion). Mutual Exclusion ensures no race-condition in the program.
It can't deadlock.
```