Java的进度条：模仿Python的tqdm

直接使用Tqdm类的静态方法tqdm迭代一个List：
```java
package weiyinfu.tqdm;

import java.util.List;

import static cn.weiyinfu.tqdm.Tqdm.tqdm;

public class Demo1 {
public static void main(String[] args) {
    for (int i : tqdm(List.of(1, 2, 3, 4, 5, 6), "iterating")) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
}

```

使用GUI的方式迭代一个List：
```java
for (int i : tqdm(List.of(1, 2, 3, 4, 5, 6), true)) {
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```
使用更灵活的方式更新进度条
```java
var x = tqdm(100, "iterating");
for (int i = 0; i < 100; i++) {
    x.update(1);
    Thread.sleep(1000);
}
```