# 12

## 12.1

> gradle cleanTest test --tests ConcurrentSetTest

Possible interleaving: 

All threads starts adding to the set at the same time. 
 - Thread 1 tries to add value 0
 - Thread 2 tries to add value 0
 - Thread 1 assert that 0 does not exist in the set
 - Thread 2 assert that 0 does not exist in the set
 - Thread 1 add 0 to the set
 - Thread 2 add 0 to the set
