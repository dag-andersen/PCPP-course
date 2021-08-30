# Exercise 1.1

## 1
Output: ```Count is 19779348 and should be 20000000```

Answer: ```The output is less than the expected 20 mio.```

## 2
Output: ```Count is 200 and should be 200```

Explain how this could be: ```The count is so small that one thread completes all iterations before the other thread starts.```

Would you say that it is guaranteed that the output is always 200: ```No``` 

## 3
Output: ```Count is 200 and should be 200```

Answer: ```No, it doesn't make a difference. why? 'count++' is syntax sugar and it's decompiled to more than one operation and therefore *race condition* will still happen.```

## 4
Output: ```hello world```

Answer: ```hello world```