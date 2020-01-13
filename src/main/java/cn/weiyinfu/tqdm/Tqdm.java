package cn.weiyinfu.tqdm;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Tqdm<T> implements Iterable<T> {


//Input
int total;//总个数
int progress = -1;//当前进度
int ncols;//控制台进度条宽度
String desc;//进度条描述
boolean gui;//是否启用GUI进度条
long printIntervalInMilli = 1000;//每隔多长时间打印一次
Iterator<T> data;//可迭代的对象
//State
long lastPrintTime = 0;
long lastProgress;
long beginTime;
char unitChar = '█';
char[] chars;
GuiProgress guiProgress;
//Default
final static int DEFAULT_NCOLS = 70;

class GuiProgress extends JFrame {
    JProgressBar progress;
    JLabel label;

    GuiProgress() {
        int width = 500, height = 200;
        this.setSize(width, height);
        this.setTitle(desc);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        progress = new JProgressBar();
        progress.setMaximum(total);
        label = new JLabel();
        label.setBackground(Color.red);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setFont(new Font("微软雅黑", Font.ITALIC, 25));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.add(progress, BorderLayout.CENTER);
        this.add(label, BorderLayout.SOUTH);
        this.setResizable(false);
        this.setVisible(true);
    }

    void complete() {
        this.setVisible(false);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.dispose();
    }
}

public static <T> Tqdm<T> tqdm(List<T> a) {
    return new Tqdm<>(a.iterator(), a.size(), "", DEFAULT_NCOLS, false);
}

public static Tqdm<Object> tqdm(int total, String desc) {
    return new Tqdm<>(null, total, desc, DEFAULT_NCOLS, false);
}

public static Tqdm<Object> tqdm(int total, String desc, boolean gui) {
    return new Tqdm<>(null, total, desc, DEFAULT_NCOLS, gui);
}

public static <T> Tqdm<T> tqdm(Iterator<T> a, int total, String desc) {
    return new Tqdm<>(a, total, desc, DEFAULT_NCOLS, false);
}

public static <T> Tqdm<T> tqdm(List<T> a, String desc) {
    return new Tqdm<>(a.iterator(), a.size(), desc, DEFAULT_NCOLS, false);
}

public static <T> Tqdm<T> tqdm(List<T> a, boolean gui) {
    return new Tqdm<>(a.iterator(), a.size(), "", DEFAULT_NCOLS, gui);
}

public static <T> Tqdm<T> tqdm(List<T> a, int width) {
    return new Tqdm<>(a.iterator(), a.size(), "", DEFAULT_NCOLS, false);
}


Tqdm(Iterator<T> a, int total, String desc, int ncols, boolean gui) {
    this.total = total;
    this.data = a;
    this.desc = desc;
    this.ncols = ncols == -1 ? DEFAULT_NCOLS : ncols;
    this.beginTime = this.lastPrintTime = System.currentTimeMillis();
    this.chars = new char[ncols];
    this.gui = gui;
    if (gui) {
        this.guiProgress = new GuiProgress();
    }
}

public void update(int delta) {
    progress += delta;
    if (System.currentTimeMillis() - lastPrintTime > printIntervalInMilli) {
        String speedString = formatSpeed(progress, lastProgress, System.currentTimeMillis() - lastPrintTime);
        double percent = 1.0 * progress / total * 100;
        String percentString = String.format("%2d%%", (int) (percent));
        long usedTime = System.currentTimeMillis() - beginTime;
        long leftTime = (long) (usedTime * 1.0 / progress * (total - progress));
        String timeString = String.format("[%s<%s,%s]", formatTime(usedTime), formatTime(leftTime), speedString);
        if (gui) {
            this.guiProgress.progress.setValue(progress);
            this.guiProgress.label.setText(String.format("%s %d/%d %s", percentString, progress, total, timeString));
            if ((data != null && !data.hasNext()) || this.progress >= this.total) {
                this.guiProgress.complete();
            }
        } else {
            StringBuilder builder = new StringBuilder("\r");
            if (desc != null) {
                builder.append(desc).append(':');
            }
            //Percent String
            builder.append(percentString);
            //GuiProgress String
            int charCount = Math.min(progress * ncols / total, chars.length);
            Arrays.fill(chars, 0, charCount, unitChar);
            Arrays.fill(chars, charCount, ncols, ' ');
            builder.append('|').append(chars).append('|');
            //GuiProgress Desc String
            builder.append(String.format("%d/%d", progress, total));
            builder.append(timeString);
            System.out.print(builder.toString());
        }
        lastPrintTime = System.currentTimeMillis();
        lastProgress = progress;
    }
}

@Override
public Iterator<T> iterator() {
    return new Iterator<T>() {
        @Override
        public boolean hasNext() {
            if (data == null) throw new RuntimeException("tqdm need set data");
            return data.hasNext();
        }

        @Override
        public T next() {
            update(1);
            return data.next();
        }
    };
}

String formatTime(long duration) {
    final int SECOND = 1000;
    final int MINUTE = 60 * SECOND;
    final int HOUR = 60 * MINUTE;
    final int DAY = 24 * HOUR;
    long day = duration / DAY;
    duration %= DAY;
    long hour = duration / HOUR;
    duration %= HOUR;
    long minute = duration / MINUTE;
    duration %= MINUTE;
    long second = duration / SECOND;
    StringBuilder builder = new StringBuilder();
    if (day > 0) {
        builder.append(String.format("%dd", day));
    }
    if (hour > 0) {
        builder.append(String.format("%dH", hour));
    }
    if (minute > 0) {
        builder.append(String.format("%dm", minute));
    }
    if (second > 0) {
        builder.append(String.format("%ds", second));
    }
    return builder.toString();
}

String formatSpeed(long currentProgress, long lastProgress, long duration) {
    long progress = currentProgress - lastProgress;
    if (progress == 1) {
        return String.format("%s/iter", formatTime(duration));
    } else {
        return String.format("%s iter/s", progress);
    }
}
}
