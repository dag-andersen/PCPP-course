

# 2.2

## 2.2.1

> Do you observe the "main" thread’s write to mi.value remains invisible to the t thread, so that it loops forever?

Yes that is exactly what we observe.

> is it possible that the program loops forever?

Yes the program loops forever because the thread `t` because it never "sees" that the main thread changed the value from 0 to 42.

## 2.2.2

> Explain why your solution prevents thread t from running forever.

