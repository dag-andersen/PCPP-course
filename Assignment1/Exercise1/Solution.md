# Exercise 1.1

## 1

> What output values do you get? Do you get the expected output, i.e., 20 million?

`Count is 19779348 and should be 20000000`


The output is less than the expected 20.000.000

## 2
Output: 

`Count is 200 and should be 200`

> Explain how this could be:

The count is so small that one thread completes all iterations before the other thread starts.

> Would you say that it is guaranteed that the output is always 200?:

No

## 3

> Do you think it would make any difference to use one of these forms instead? Why? Change the code andrun it. Do you see any difference in the results for any of these alternatives?

No, it doesn't make a difference. why? 'count++' and `counter += 1` are syntax sugar and it's compiled down to more than one operation and therefore *race conditions* can still happen

## 4

> Explain why your solution is correct, and why no other output is possible.

Code:
```
lock.lock();
count++;
lock.unlock();
```

Notes: 

- count++ is the critical section.
- There is no race-condition
- There is mutual-exclusion
- The number of interleavings are finite.
- It can't deadlock.


Lets define "correct": The program always prints 20_000_000.

The program is correct since the critical section can only be accessed by one thread at the time (mutual exclusion). Mutual Exclusion ensures no race-condition in the program.
It can't deadlock.

# Exercise 1.2

## 1
> Write a program that creates a Printer objectp, and then creates and starts two threads.  Each thread mustcallp.print()forever.

See code.

## 2
> Describe an interleaving where two bars are printed in a row, or two dashes are printed in a row,creating small “weaving faults”.

Thread 1 starts and runs the first operation `system.out.print("-");` and proceeds to the `Thread.sleep(50);` operation, and before it reaches the next operation (`System.out.print("|");`), Thread 2 starts and runs the `system.out.print("-");` operation.

## 3
>  Explain why your solution is correct, and why it is not possible for incorrect patterns, such as in the output above, to appear.

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

## 1
> Modify the behaviour of theTurnstilethread class so that that exactly 15000 enter the park; no lessno  more.

See code.

## 2

> Explain why your solution is correct, and why it always output 15000.

Lets define "correct": The program always prints 15000.

The critical section is:
`counter++;`

The program is correct because there is no race-conditions, since only one thread can enter the critical section at the time (mutual exclusion).


# Exercise 1.4

## 1

> Find some examples of systems which areincluded in the categories of Goetz, but not in those in the concurrency note, and vice versa (if possible - ifnot possible, argue why).

Included in Goetz but not in notes:
- Convenience: most of the programs on my machine is standalone programs. It is easier to program and manipulate individually a single standalone program, which is more convenient - but the developers could also just write one huge monolith program that handles everything. This case of motivation is not covered in the notes.

Included in the notes but not in Goetz:
- Hidden: Letting each program use shared resourced and ignore the fact that other programs may use the same resource. E.g. if a program call a database with transactions it will not care about if other programs interact with the database as well.

## 2

> Find examples of 3 systems in each of the categories in the Concurrency note which you have used yourself(as a programmer or user).

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
