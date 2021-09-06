

# 2.2

## 2.2.1

> Do you observe the "main" thread’s write to mi.value remains invisible to the t thread, so that it loops forever?

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






