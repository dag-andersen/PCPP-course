# 4.1.2

Thread safe definition:
> A class is said to be thread-safe if and only if no concurrent execution of method calls or field accesses (read/write) result in race conditions

To analyse thread-safe in a class, we must identify/consider:
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

# 4.2.2

I would assume that the constructor is safe, since accessing an object's fields are not possible before it has been constructed. Every time `Person.idCounter` is used i synchronize/lock the static variable.

# 4.2.4 
No you can never be sure.






