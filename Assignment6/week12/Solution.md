# 12

> gradle cleanTest test --tests ConcurrentSetTest

## 12.1 (ADD)

Possible interleaving: 

All threads starts adding to the set at the same time. 
 - Thread 1 tries to add value 42
 - Thread 2 tries to add value 42
 - Thread 1 assert that 42 does not exist in the set
 - Thread 2 assert that 42 does not exist in the set
 - Thread 1 add 42 to the set
 - Thread 1 increment the size of the set
 - Thread 2 add 42 to the set (properly fails?)
 - Thread 2 increment the size of the set

## 12.2 (REMOVE)

Possible interleaving: 

All threads starts adding to the set at the same time. 
 - Thread 1 tries remove value 42
 - Thread 2 tries remove value 42
 - Thread 1 assert that 42 does exist in the set
 - Thread 2 assert that 42 does exist in the set
 - Thread 1 remove 42 from the set
 - Thread 1 decrement the size of the set
 - Thread 2 remove 42 from the set (properly fails?)
 - Thread 2 decrement the size of the set

 ## 12.3

 Wrapping the method `Collections.synchronizedSet(Set<T> s)` around the HashSet works by synchronizing the whole collection. This could potentially have a negative impact on performance with all the locks, but it does prevent the interleavings from the previous exercises from happening.

## 12.4

All test passes. This is because the ConcurrentSkipListSet does not hold a count on all the elements it contains, instead it requires a traversal of the elements when getting the size of the set. In our test cases we are not requiring the size of the set before the assertion and when all modifications to the set has happened. If we had a method that would modify the set based on the size, we would likely run into problems while using this method concurrently with the `add` and `remove` method.

## 12.5

Possible interleaving: 

All threads starts adding to the set at the same time. 
 - Thread 1 start to traverse set to get size
 - Thread 2 start to traverse set to get size
 - Thread 1 find size 100_000
 - Thread 1 remove 0
 - Thread 2 find size 100_000
 - Thread 1 start to traverse set to get size
 - Thread 2 remove 0 (fails, since it is already removed)
 - Thread 2 start to traverse set to get size
 - Thread 1 find size 99_000
 - Thread 1 remove 1
 - Thread 2 find size 99_000
 - ....

 Since the size of the set is modified during traversal to get the size, we end up with unsuspected results.

 