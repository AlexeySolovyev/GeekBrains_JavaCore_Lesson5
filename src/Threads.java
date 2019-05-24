import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Threads {

    static final int SIZE = 10000000;
    static final int H = SIZE / 2;

    public static void calc(float[] arr, int cnt, int offset) {
        for (int i = offset; i < cnt; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        float[] array = new float[SIZE];
        long stTime;

        Arrays.fill(array, 1);

        float[] arr10 = array;

        stTime = System.currentTimeMillis();  // реализация без потоков
        calc(array, SIZE, 0);
        System.out.printf(" without thread time: %d%n", System.currentTimeMillis() - stTime);

        stTime = System.currentTimeMillis();  // реализация с потоками с наследованием от Thread и без добавления arraycopry
        List<Thread> threadList = new ArrayList<>();


        for (int i = 0; i < 2; i++) {
            Thread newThread = new CalcThread(arr10, H, i * H);
            threadList.add(newThread);
            newThread.start();
        }

        for (Thread thr : threadList) {
            thr.join();
        }
        System.out.printf(" thread time: %d%n", System.currentTimeMillis() - stTime);


        stTime = System.currentTimeMillis(); // реализация с потоками с имплеминтацией интерфейса Runnable и с добавлением arraycopy

        float[] array1 = new float[H];
        float[] array2 = new float[H];

        System.arraycopy(array, 0, array1, 0, H);
        System.arraycopy(array, H, array2, 0, H);
        List<Thread> threadList2 = new ArrayList<>();
        Thread runnableCalc1 = new Thread(new CalcRunnable(array1, H, 0));
        threadList2.add(runnableCalc1);
        runnableCalc1.start();
        Thread runnableCalc12 = new Thread(new CalcRunnable(array2, H, 0));
        threadList2.add(runnableCalc12);
        runnableCalc12.start();
        for (Thread thr : threadList2) {
            thr.join();
        }
        System.arraycopy(array1, 0, array, 0, H);
        System.arraycopy(array2, 0, array, H, H);

        System.out.printf(" thread with copy time: %d%n", System.currentTimeMillis() - stTime);
    }
}
