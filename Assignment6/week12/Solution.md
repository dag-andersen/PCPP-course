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