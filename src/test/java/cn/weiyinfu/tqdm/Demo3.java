package cn.weiyinfu.tqdm;

import static cn.weiyinfu.tqdm.Tqdm.tqdm;

public class Demo3 {
public static void main(String[] args) throws InterruptedException {
    var x = Tqdm.tqdm(100, "iterating");
    for (int i = 0; i < 100; i++) {
        x.update(1);
        Thread.sleep(10000);
    }
}
}
