# 6

## 6.1

### 6.1.1
`
gradle -PmainClass=org.example.exercises6.AccountExperiments run
`

```
1 Transactions :               50255761.0 ns   55545.93          8
2 Transactions :              100466500.8 ns   52705.03          4
4 Transactions :              200829327.0 ns   87560.30          2
8 Transactions :              401576355.9 ns  291092.33          2
16 Transactions :             802867898.9 ns  377028.23          2
```

### 6.1.2

It is necessary to order the way the transfer locks the accounts, to prevent deadlocks from happening. If the code didn't lock the accounts *min* first *max* last, an interleaving could happen where thread 1 locks the *min* account and before it locks the *max* account thread 2 locks the *max*, creating a deadlock where both threads waits for the other to release the lock.

`gradle -PmainClass=org.example.exercises6.ThreadsAccountExperimentsMany run`

With ordered locks:
```
Transfer 4377 from 1 to 8
Transfer 1332 from 4 to 0
Transfer 1531 from 5 to 9
Transfer 2043 from 0 to 2
Transfer 766 from 9 to 7
Transfer 947 from 8 to 3
Transfer 1822 from 5 to 6
Transfer 466 from 3 to 5
Transfer 1215 from 5 to 8
Transfer 4180 from 6 to 9
Transfer 463 from 1 to 4
Transfer 1824 from 6 to 8
Transfer 4147 from 9 to 2
Transfer 2330 from 4 to 2
Transfer 2197 from 8 to 4
Transfer 888 from 9 to 7
Transfer 1543 from 9 to 0
Transfer 4510 from 7 to 9
Transfer 4232 from 9 to 4
Transfer 2336 from 4 to 5
Transfer 1655 from 6 to 4
Transfer 3541 from 6 to 9
Transfer 2838 from 8 to 9
Transfer 1480 from 5 to 6
Transfer 3312 from 0 to 8
Transfer 1017 from 7 to 3
Transfer 3683 from 3 to 5
Transfer 1468 from 8 to 3
Transfer 952 from 2 to 0
Transfer 4088 from 0 to 5
Transfer 3697 from 1 to 5
Transfer 3593 from 7 to 3
Transfer 1715 from 2 to 9
Transfer 4856 from 4 to 6
Transfer 3282 from 2 to 5
Transfer 2813 from 9 to 5
Transfer 3782 from 6 to 0
Transfer 1807 from 7 to 3
Transfer 4551 from 2 to 9
Transfer 2210 from 2 to 3
Transfer 3042 from 7 to 3
Transfer 2860 from 2 to 3
Transfer 432 from 7 to 4
Transfer 980 from 2 to 0
Transfer 3557 from 8 to 3
Transfer 2056 from 0 to 6
Transfer 981 from 3 to 9
Transfer 3295 from 1 to 3
Transfer 3674 from 8 to 6
Transfer 4859 from 3 to 5
```

Without ordered locks:
```
Transfer 517 from 6 to 8
Transfer 3839 from 7 to 4
Transfer 2368 from 8 to 0
Transfer 519 from 0 to 8
Transfer 4951 from 9 to 6
Transfer 852 from 6 to 9
Transfer 2179 from 2 to 0
Transfer 3426 from 9 to 4
Transfer 3475 from 3 to 9
Transfer 2205 from 0 to 2
Transfer 295 from 0 to 5
Transfer 5034 from 3 to 6
Transfer 3127 from 7 to 8
Transfer 1842 from 1 to 8
Transfer 4542 from 3 to 8
Transfer 443 from 4 to 5
Transfer 756 from 8 to 1
Transfer 2223 from 1 to 5
Transfer 2558 from 5 to 1
Transfer 394 from 7 to 8
Transfer 3519 from 8 to 3
...never finish executing...
```

The results show exactly what was mentioned above. The code with the ordered locks executes and finishes all transactions, while the code without the ordered locks goes into a deadlock not even halfway through the transactions.

### 6.1.3

### 6.1.4

## 6.2

### 6.2.1


### 6.2.2


## 6.3

### 6.3.1

### 6.3.2



