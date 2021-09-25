# 4

Command: `gradle -PmainClass=exercises04.BoundedBuffer run`

# 4.1.2


Thread safe definition:
> A class is said to be thread-safe if and only if no concurrent execution of method calls or field accesses (read/write) result in race conditions

To analyze thread-safe in a class, we must identify/consider:
- Identify the class state
    > The state is only accessed behind a "lock"
- Make sure that mutable class state does not escape
    > The state does not escape itself... but each item in the queue escapes. But an item in the queue can be modified by the insert-caller. E.g if the caller inserts X object in the queue and then changes a property on X afterwards. 
- Ensure safe publication
    > Only the items are publicized... and only one consumer will get a reference to that item. 
- Whenever possible define class state as immutable
    > The internal datastructure is final, so the reference is immutable. But the items in the list are mutable.
- If class state must be mutable, ensure mutual exclusion
    > The linkedlist has mutual exclusion access, so there is no internal data races. 


# 4.1.3

No I cant come up with a solution where barriers help. Barriers would block until at curtain amount of threads reach that a curtain point. The solution would end up blocking the queue until was full.

--------------------------------------------------------------------------

# 4.2
Command: `gradle -PmainClass=exercises04.Person run --console="plain"`

## 4.2.1
Check implementation.

## 4.2.2
Explain why your implementation of the Person constructor is thread-safe
> Subsequent accesses will not refer to a partially created object BECAUSE the id is final, which ensures happens-before relation. The static person counter `Person.idCounter` is synchronized/locked based on the class itself, so it should not be possible to have any racecoditions. The reason it locks on `Person.class` is because you can't lock on a primitive like `idCounter` (long).

## 4.2.3
Check implementation. 

## 4.2.4 
No you can never be sure. I don't know how to prove it is thread safe.






