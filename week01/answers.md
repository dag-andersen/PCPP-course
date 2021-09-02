# Exercise 1.1

## 1
Output: ```Count is 19779348 and should be 20000000```

Answer: 

`The output is less than the expected 20 mio.`

## 2
Output: `Count is 200 and should be 200`

Explain how this could be:
```The count is so small that one thread completes all iterations before the other thread starts.```

Would you say that it is guaranteed that the output is always 200?:
`No`

## 3


No, it doesn't make a difference. why? 'count++' and `counter += 1` are syntax sugar and it's decompiled to more than one operation and therefore *race conditions* can still happen

## 4

Question:
> Explain why your solution is correct, and why no other output is possible.

Code:
```
lock.lock();
count++;
lock.unlock();
```

Notes: 

count++ is the critical section.
There is no race-condition
There is mutual-exclusion
The number of interleavings are finite.
It can't deadlock.


Answer:

Lets define "correct": The program always prints 20_000_000.

The program is correct since the critical section can only be accessed by one thread at the time (mutual exclusion). Mutual Exclusion ensures no race-condition in the program.
It can't deadlock.

# Exercise 1.2

## 1
See code

## 2

Thread 1 starts and runs the first operation `system.out.print("-");` and proceeds to the `Thread.sleep(50);` operation, and before it reaches the next operation (`System.out.print("|");`), Thread 2 starts and runs the `system.out.print("-");` operation.

## 3

The critical section is:
```
System.out.print("-");
try {
    Thread.sleep(50);
} catch (InterruptedException exn) { }
System.out.print("|");
```

Lets define "correct": The program always prints the correct pattern.

The program is correct because there is no race-conditions, since only one thread can enter the critical section at the time (mutual exclusion).


# Exercise 1.3

## 2

Lets define "correct": The program always prints 15000.

The critical section is:
`counter++;`

The program is correct because there is no race-conditions, since only one thread can enter the critical section at the time (mutual exclusion).


# Exercise 1.4

## 1

Included in Goetz but not in notes:
- Convenience: most of the programs on my machine is standalone programs. It is easier to program and manipulate individually a single standalone program, which is more convenient - but the developers could also just write one huge monolith program that handles everything. This case of motivation is not covered in the notes.

Included in the notes but not in Goetz:
- Hidden: Letting each program use shared resourced and ignore the fact that other programs may use the same resource. E.g. if a program call a database with transactions it will not care about if other programs interact with the database as well.

## 2

Inherent:
- Ordering food online - i can do other stuff until the delivery arrives.
- Downloading a file. Starting a download in the background and coming back later to use the file.
- `Awaits` in code - being able to do something else while waiting for a response.

Exploitation:
- SSH'ing to the same machine.
- Running multiple containers on the same computer
- Watching a movie. Computer plays sound and streams video at the same time.

Hidden:
- Watching a movie in the cinema. I doesn't matter if i am alone or not. I watch the movie (the resource) as i am the only consumer.
- Calling a database with transactions
- Atomics in programming
