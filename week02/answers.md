# 2.1
## 2.1.1
> Use Java Intrinsic Locks (i.e., synchronized) to implement a monitor ensuring that the access to the shared resource by reader and writer threads is according to the specification above.

See code.

## 2.1.2

> Does your solution ensure that if a writer thread wants to write, then it will eventually do so? If so, explain why.

The solution is fair towards writers because when a thread wants to write it will immediately set `writer = true;` blocking new readers, which ensures that the thread will eventually get to write as soon as the current readers stops reading. If the `writer` variable is already `true` the writer will have to wait for the current writer to finish.

# 2.2

## 2.2.1

> Do you observe the "main" thread’s write to `mi.value` remains invisible to the t thread, so that it loops forever?

Yes that is exactly what we observe.

> is it possible that the program loops forever?

Yes the program loops forever because the thread `t` because it never "sees" that the main thread changed the value from 0 to 42. The updated value of `MutableInteger` is never flushed to memory, and the the new value may not be visible to the t thread. 

## 2.2.2

> Use  Java  Intrinsic  Locks  (synchronized)  on  the  methods  of  theMutableIntegerto  ensure  that thread t always terminates.

```java
class MutableInteger {
	private int value = 0;

	public synchronized void set(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}
}
```

> Explain why your solution prevents thread t from running forever.

Because using the `synchronized` ensures that there is a *happens-before* relation between the reading and writing to the MutableInteger. Consequently, the CPU is not allowed to keep the value of running in the register of the CPU or cache and must flush it to main memory.

## 2.2.3

> Would thread t always terminate if get() is not defined as synchronized? Explain your answer.

no, because if the `synchronized` keyword is only used when writing a *happens-before* relation cant exist. 

## 2.2.4
> Remove all the locks in the program, and define value in MutableInteger as a volatile variable.

```java
class MutableInteger {
	volatile private int value = 0;

	public void set(int value) {
		this.value = value;
	}

	public int get() {
		return value;
	}
}
```

> Does thread t always terminate in this case? Explain your answer.
Yes because volatile variables are not stored in CPU registers or low levels of cache hidden from other CPUs•Writes to volatile variables flush registers low level cache to shared memory levels. Therefore thread t will always read the MutableInteger as 42.

## 2.3.1

> Compile the program and run it several times. Show the results you get.
```
es02.TestLocking0 
Sum is 1641344,000000 and should be 2000000,000000
```

```
es02.TestLocking0 
Sum is 1131776,000000 and should be 2000000,000000
```

```
es02.TestLocking0 
Sum is 1160960,000000 and should be 2000000,000000
```
> Are there any race conditions?

yes.

# 2.3.2 
> Explain why race conditions appear when t1 and t2 use the Mystery object. Hint: Consider (a) what it means for an instance method to be synchronized, and (b) what it means for a static method to be synchronized.

When you call a synchronized method on an instance object, it synchronizes with self (the instance object itself). But when you call synchronized on a static method (on the static object), then it synchronized with the static object. Since the instance object and static object are the not the same, then there is no guarantee that the value is flushed and the changes in one thread my not be visible to another thread. Therefore race conditions can appear.

