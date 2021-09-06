# 2.1
## 2.1.1
> Use Java Intrinsic Locks (i.e., synchronized) to implement a monitor ensuring that the access to the
shared resource by reader and writer threads is according to the specification above.

See code.

## 2.1.2

> Does your solution ensure that if a writer
thread wants to write, then it will eventually do so? If so, explain why.

The solution is fair towards writers because when a thread wants to write it will immediately set `writer = true;` blocking new readers from acquiring the lock, which ensures that the thread will eventually get to write as soon as the current readers stops reading.

# 2.2

## 2.2.1

> Do you observe the "main" thread’s write to mi.value remains invisible to the t thread, so that it loops forever?

Yes that is exactly what we observe.

> is it possible that the program loops forever?

Yes the program loops forever because the thread `t` because it never "sees" that the main thread changed the value from 0 to 42.

## 2.2.2

> Explain why your solution prevents thread t from running forever.

