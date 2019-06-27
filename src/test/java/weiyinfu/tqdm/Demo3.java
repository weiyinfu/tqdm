package weiyinfu.tqdm;

import static weiyinfu.tqdm.Tqdm.tqdm;

public class Demo3 {
public static void main(String[] args) throws InterruptedException {
    var x = tqdm(100, "iterating");
    for (int i = 0; i < 100; i++) {
        x.update(1);
        Thread.sleep(1000);
    }
}
}
