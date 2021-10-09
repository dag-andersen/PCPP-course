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

`gradle -PmainClass=org.example.exercises6.ThreadsAccountExperimentsMany run`

```
Transfer:  2171 from 8 to 0   PoolActiveCount:   8
Transfer:  3979 from 4 to 5   PoolActiveCount:   7
Transfer:  1862 from 5 to 9   PoolActiveCount:  10
Transfer:  3081 from 1 to 4   PoolActiveCount:   7
Transfer:  4329 from 4 to 9   PoolActiveCount:   7
Transfer:  1753 from 3 to 6   PoolActiveCount:  10
Transfer:  3069 from 7 to 2   PoolActiveCount:   7
Transfer:  1392 from 1 to 7   PoolActiveCount:   6
Transfer:  2366 from 6 to 3   PoolActiveCount:   7
Transfer:   203 from 2 to 0   PoolActiveCount:   9
Transfer:   972 from 8 to 0   PoolActiveCount:  10
Transfer:  3629 from 4 to 2   PoolActiveCount:   9
Transfer:  4604 from 5 to 2   PoolActiveCount:  10
Transfer:  4538 from 8 to 4   PoolActiveCount:  10
Transfer:  4688 from 8 to 6   PoolActiveCount:  10
Transfer:  4476 from 6 to 9   PoolActiveCount:  10
Transfer:  2129 from 6 to 0   PoolActiveCount:  10
Transfer:  4247 from 5 to 3   PoolActiveCount:  10
Transfer:   801 from 3 to 8   PoolActiveCount:  10
Transfer:  3666 from 0 to 1   PoolActiveCount:  10
Transfer:  1390 from 9 to 3   PoolActiveCount:  10
Transfer:   823 from 4 to 5   PoolActiveCount:   9
Transfer:  3401 from 4 to 2   PoolActiveCount:  10
Transfer:  4047 from 8 to 1   PoolActiveCount:  10
Transfer:  1861 from 7 to 9   PoolActiveCount:  10
Transfer:  4184 from 3 to 5   PoolActiveCount:  10
Transfer:  3210 from 0 to 4   PoolActiveCount:  10
Transfer:  2605 from 6 to 3   PoolActiveCount:  10
Transfer:   544 from 8 to 1   PoolActiveCount:  10
Transfer:  1216 from 2 to 5   PoolActiveCount:  10
Transfer:  3052 from 4 to 8   PoolActiveCount:   9
Transfer:   589 from 5 to 9   PoolActiveCount:  10
Transfer:  3142 from 6 to 7   PoolActiveCount:  10
Transfer:  3262 from 7 to 9   PoolActiveCount:  10
Transfer:   534 from 2 to 0   PoolActiveCount:  10
Transfer:  3387 from 8 to 2   PoolActiveCount:  10
Transfer:  2067 from 3 to 5   PoolActiveCount:  10
Transfer:  3895 from 4 to 2   PoolActiveCount:  10
Transfer:  3186 from 3 to 9   PoolActiveCount:  10
Transfer:  3911 from 0 to 1   PoolActiveCount:  10
Transfer:   671 from 1 to 2   PoolActiveCount:  10
Transfer:  2652 from 0 to 1   PoolActiveCount:  10
Transfer:  4428 from 9 to 2   PoolActiveCount:  10
Transfer:  4874 from 2 to 5   PoolActiveCount:  10
Transfer:  2882 from 7 to 0   PoolActiveCount:  10
Transfer:  3880 from 9 to 6   PoolActiveCount:  10
Transfer:  4036 from 2 to 7   PoolActiveCount:  10
Transfer:  2958 from 7 to 8   PoolActiveCount:  10
Transfer:  1978 from 6 to 0   PoolActiveCount:  10
Transfer:  4017 from 5 to 2   PoolActiveCount:  10
```

### 6.1.4

`gradle -PmainClass=org.example.exercises6.ThreadsAccountExperimentsMany run`

```
Transfer:  4279 from 3 to 9   PoolActiveCount:   8
Transfer:  4134 from 2 to 3   PoolActiveCount:  10
Transfer:  4663 from 1 to 3   PoolActiveCount:   8
Transfer:  2850 from 3 to 6   PoolActiveCount:   7
Transfer:  1115 from 0 to 3   PoolActiveCount:  10
Transfer:  4146 from 4 to 6   PoolActiveCount:   8
Transfer:  3198 from 2 to 5   PoolActiveCount:   7
Transfer:  3667 from 3 to 0   PoolActiveCount:   9
Transfer:  1764 from 2 to 3   PoolActiveCount:   7
Transfer:   784 from 1 to 2   PoolActiveCount:   8
Transfer:  3656 from 8 to 4   PoolActiveCount:  10
Transfer:  4045 from 6 to 8   PoolActiveCount:  10
Transfer:  3169 from 4 to 9   PoolActiveCount:  10
Transfer:  3080 from 9 to 6   PoolActiveCount:  10
Transfer:  3814 from 8 to 3   PoolActiveCount:  10
Transfer:  1925 from 3 to 6   PoolActiveCount:  10
Transfer:  2115 from 5 to 0   PoolActiveCount:  10
Transfer:  3468 from 7 to 1   PoolActiveCount:  10
Transfer:  3610 from 4 to 1   PoolActiveCount:  10
Transfer:   645 from 4 to 5   PoolActiveCount:  10
Transfer:  1289 from 1 to 9   PoolActiveCount:   9
Transfer:  4861 from 1 to 2   PoolActiveCount:  10
Transfer:  4302 from 5 to 1   PoolActiveCount:  10
Transfer:   476 from 0 to 2   PoolActiveCount:  10
Transfer:   343 from 7 to 0   PoolActiveCount:  10
Transfer:  2627 from 9 to 5   PoolActiveCount:  10
Transfer:  2898 from 4 to 2   PoolActiveCount:  10
Transfer:  2471 from 2 to 8   PoolActiveCount:  10
Transfer:  3846 from 8 to 4   PoolActiveCount:  10
Transfer:  4091 from 1 to 8   PoolActiveCount:  10
Transfer:  1771 from 7 to 8   PoolActiveCount:  10
Transfer:  2796 from 6 to 7   PoolActiveCount:  10
Transfer:  2888 from 1 to 3   PoolActiveCount:  10
Transfer:  1625 from 1 to 2   PoolActiveCount:  10
Transfer:  3627 from 3 to 5   PoolActiveCount:  10
Transfer:   387 from 4 to 0   PoolActiveCount:  10
Transfer:  1113 from 6 to 2   PoolActiveCount:  10
Transfer:   410 from 8 to 0   PoolActiveCount:  10
Transfer:  3280 from 4 to 0   PoolActiveCount:  10
Transfer:   330 from 4 to 2   PoolActiveCount:  10
Transfer:  4315 from 8 to 6   PoolActiveCount:  10
Transfer:  3298 from 8 to 0   PoolActiveCount:  10
Transfer:  2898 from 2 to 3   PoolActiveCount:  10
Transfer:   893 from 0 to 1   PoolActiveCount:  10
Transfer:  3782 from 2 to 4   PoolActiveCount:  10
Transfer:   791 from 3 to 9   PoolActiveCount:  10
Transfer:  1466 from 5 to 1   PoolActiveCount:  10
Transfer:  3520 from 1 to 3   PoolActiveCount:  10
Transfer:  3781 from 2 to 6   PoolActiveCount:  10
Transfer:  2193 from 1 to 3   PoolActiveCount:  10

Total number of tasks: 50
Pool isShutdown(): true
```

## 6.2

### 6.2.1

`gradle -PmainClass=org.example.exercises6.TestCountPrimesThreads run`

```
> Task :run
countSequential                11758016,4 ns 1221146,18         32
countParallelN  1               9514672,8 ns  772878,34         32
countParallelNLocal  1         10796756,8 ns  670485,49         32
countParallelN  2               8553553,4 ns 2528674,36         32
countParallelNLocal  2          7834105,2 ns  351349,96         32
countParallelN  3               6506091,7 ns  782426,90         64
countParallelNLocal  3          6447525,6 ns  487803,44         64
countParallelN  4               4634838,1 ns  326948,46         64
countParallelNLocal  4          5597435,0 ns  583789,90         64
countParallelN  5               5432779,1 ns  899237,44         64
countParallelNLocal  5          7421265,0 ns 2237981,54         64
countParallelN  6               6589752,6 ns 1111896,52         64
countParallelNLocal  6          6888625,5 ns 1061676,16         64
countParallelN  7               4917982,6 ns  631859,04         64
countParallelNLocal  7          6084972,9 ns  631282,91         64
countParallelN  8               4730483,7 ns  180522,49         64
countParallelNLocal  8          6122596,0 ns 1423380,29         64
countParallelN  9               4970632,7 ns  458212,58         64
countParallelNLocal  9          6491240,0 ns 1584251,41         64
countParallelN 10               5004648,1 ns  519320,49         64
countParallelNLocal 10          5281487,4 ns  112142,96         64
countParallelN 11               4967768,9 ns  651840,93         64
countParallelNLocal 11          5181369,1 ns   99521,90         64
countParallelN 12               4507421,4 ns  119486,66         64
countParallelNLocal 12          5223524,6 ns  157792,58         64
countParallelN 13               4587724,4 ns  117525,53         64
countParallelNLocal 13          5589714,8 ns  781855,06         64
countParallelN 14               4931208,6 ns  273970,61         64
countParallelNLocal 14          7744005,6 ns 2617511,32         32
countParallelN 15               7667419,0 ns 2418312,74         32
countParallelNLocal 15          5720381,1 ns  590867,39         64
countParallelN 16               4740009,8 ns  322761,92         64
countParallelNLocal 16          9987077,6 ns 5308734,20         16
countParallelN 17              13832468,5 ns 4831315,40         32
countParallelNLocal 17         10043260,2 ns 3338865,48         64
countParallelN 18               7522804,1 ns 2200817,63         64
countParallelNLocal 18          8932486,4 ns 2841852,62         32
countParallelN 19              11976849,1 ns 1563356,27         32
countParallelNLocal 19         10871346,3 ns 2273246,12         32
countParallelN 20              10145021,9 ns 1826454,18         32
countParallelNLocal 20          7077905,5 ns 1488777,78         32
countParallelN 21               8999739,3 ns 2624710,93         64
countParallelNLocal 21          8015354,7 ns 1726174,93         32
countParallelN 22               4870701,6 ns  104183,85         64
countParallelNLocal 22          6716060,7 ns 1540701,03         64
countParallelN 23               7113380,3 ns 2433895,94         64
countParallelNLocal 23         13210799,1 ns 4154590,02         32
countParallelN 24               9547882,7 ns 2232809,72         64
countParallelNLocal 24          6384067,4 ns  283702,92         64
countParallelN 25               5603469,3 ns  627692,14         64
countParallelNLocal 25          7526125,7 ns 1606587,34         32
countParallelN 26               5799452,0 ns  393710,61         64
countParallelNLocal 26          6865248,8 ns 1737456,25         32
countParallelN 27               5141808,4 ns  284187,12         64
countParallelNLocal 27          7704844,8 ns 2516787,83         64
countParallelN 28               6369349,1 ns 1499569,30         64
countParallelNLocal 28          6001101,5 ns  530985,64         64
countParallelN 29               5135704,0 ns  113978,59         64
countParallelNLocal 29          7211219,9 ns 1833438,56         64
countParallelN 30               6729882,2 ns 1657597,75         32
countParallelNLocal 30          8051193,7 ns 3257885,61         64
countParallelN 31               7373818,7 ns 2805062,12         32
countParallelNLocal 31          7623396,2 ns 2634503,32         64
countParallelN 32               5279250,8 ns  312123,23         64
countParallelNLocal 32          8202147,4 ns 4333091,66         64
```

On this mac on average countParallelN is faster than countParallelNLocal... but it is not much.
Both functions are pretty slow, since creating new threads are expensive. 


### 6.2.2

```
countSequential                12300697,3 ns 2001315,15         16
countParallelN  1              11854995,7 ns 1607923,96         32
countParallelNLocal  1         10317676,7 ns  374457,15         32
countParallelNPool  1               722,2 ns     565,35     262144
countParallelN  2              11096109,7 ns   82520,86         32
countParallelNLocal  2         11406972,3 ns 3876281,00         32
countParallelNPool  2              1756,2 ns    1053,40     524288
countParallelN  3              10301107,3 ns  557045,62         32
countParallelNLocal  3          9466289,3 ns 1133579,13         32
countParallelNPool  3              2471,7 ns    2172,01     131072
countParallelN  4               8261462,3 ns  212698,90         32
countParallelNLocal  4          7740132,4 ns  280965,49         64
countParallelNPool  4             55341,6 ns  103563,73      32768
countParallelN  5               7871683,8 ns  189219,30         32
countParallelNLocal  5          7312722,2 ns  213476,73         64
```

The threadpool function performs much better.


## 6.3

### 6.3.1

### 6.3.2



