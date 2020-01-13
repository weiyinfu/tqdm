package cn.weiyinfu.tqdm;

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
