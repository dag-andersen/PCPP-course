# 12

> gradle cleanTest test --tests ConcurrentSetTest

## 12.1 - Test Add method

Possible interleaving:

All threads starts adding to the set at the same time.

- Thread 1 tries to add value 42
- Thread 2 tries to add value 42
- Thread 1 assert that 42 does not exist in the set
- Thread 2 assert that 42 does not exist in the set
- Thread 1 add 42 to the set
- Thread 1 increment the size of the set
- Thread 2 add 42 to the set (overwrite the 42 added by thread 1)
- Thread 2 increment the size of the set

At this point the size() of the set could be above its actual size.

## 12.2 - Test Remove method

Possible interleaving:

All threads starts removing from the set at the same time.

- Thread 1 tries remove value 42
- Thread 2 tries remove value 42
- Thread 1 assert that 42 does exist in the set
- Thread 2 assert that 42 does exist in the set
- Thread 1 remove 42 from the set
- Thread 1 decrement the size of the set
- Thread 2 remove 42 from the set - (no element is removed since 42 doesn't exist)
- Thread 2 decrement the size of the set

At this point the size() of the set could be below its actual size.

## 12.3 - Thread-safe Set implementation

Wrapping the method `Collections.synchronizedSet(Set<T> s)` around the HashSet works by synchronizing the whole collection. This could potentially have a negative impact on performance with all the locks, but it does prevent the interleavings from the previous exercises from happening.

## 12.4 - ConcurrentSkipListSet

All test passes. This is because the ConcurrentSkipListSet does not hold a count on all the elements it contains, instead it requires a traversal of the elements when getting the size of the set. In our test cases we are not requiring the size of the set before the assertion and when all modifications to the set has happened. If we had a method that would modify the set based on the size, we would likely run into problems while using this method concurrently with the `add` and `remove` method.

## 12.5 - Prove non thread-safe

Possible interleaving:

All threads starts removing from the set at the same time.

- Thread 1 starts to traverse set to get size
- Thread 2 starts to traverse set to get size
- Thread 1 finds size 100_000
- Thread 1 removes 0
- Thread 2 finds size 100_000
- Thread 1 starts to traverse set to get size
- Thread 2 removes 0 - fails, since it is already removed)
- Thread 2 starts to traverse set to get size
- Thread 1 finds size 99_000
- Thread 1 removes 1
- Thread 2 finds size 99_000
- ....

Since the size of the set is modified during traversal to get the size, we end up with unsuspected results.

## 12.6 - Discuss thread-safety

“_Program testing can be used to show the presence of bugs, but never to show their absence!_” - Edsger W. Dijkstra
