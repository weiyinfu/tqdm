package weiyinfu.tqdm;

import static weiyinfu.tqdm.Tqdm.tqdm;

public class Demo4 {
public static void main(String[] args) {
    int n = 100;
    var x = tqdm(n, "haha", true);
    for (int i = 0; i < n; i++) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        x.update(1);
    }
}
}
