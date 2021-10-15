package ua.kovalev;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int[] array = new Random().ints(1, 101).limit(1_000_000).toArray();
        int[] array2 = array.clone();

        /* --- тест с одним потоком --- */
        int sizePackage = 1;
        Thread[] threads = new Thread[sizePackage];
        Runnable[] runnables = new SortByShellRunnable[sizePackage];
        createSortThreadsAndRunnables(threads, runnables, array);

        long startTime = System.currentTimeMillis();
        startThreads(threads);
        long endTime = System.currentTimeMillis();

        System.out.println(String.format("кол-во потоков: %d, время сортировки: %f c.",
                threads.length, (endTime - startTime) / 1000D));

/*        System.out.println(String.format("кол-во потоков: %d, время сортировки: %f c., \n%s",
                threads.length, (endTime - startTime) / 1000D,
                Arrays.toString(array)));*/

        /* --- тест с кол-вом потоков равное кол-ву ядер в процессоре --- */
        sizePackage = Runtime.getRuntime().availableProcessors();

        threads = new Thread[sizePackage];
        runnables = new SortByShellRunnable[sizePackage];
        createSortThreadsAndRunnables(threads, runnables, array2);

        startTime = System.currentTimeMillis();
        startThreads(threads);
        endTime = System.currentTimeMillis();

        joinArrays(runnables, array2);

        System.out.println(String.format("кол-во потоков: %d, время сортировки: %f c.",
                threads.length, (endTime - startTime) / 1000D));

/*        System.out.println(String.format("кол-во потоков: %d, время сортировки: %f c., \n%s",
        threads.length, (endTime - startTime) / 1000D,
        Arrays.toString(array2)));*/
    }

    static void createSortThreadsAndRunnables(Thread[] threads, Runnable[] runnables, int[] array) {
        int sizePackage = array.length / threads.length;

        int curBeginIndex;
        int curEndIndex;
        for (int i = 0; i < threads.length; i++) {
            curBeginIndex = sizePackage * i;
            if (i == (threads.length - 1)) {
                curEndIndex = array.length - 1;
            } else {
                curEndIndex = curBeginIndex + (sizePackage - 1);
            }
            Runnable runnable = new SortByShellRunnable(array, curBeginIndex, curEndIndex);
            runnables[i] = runnable;
            threads[i] = new Thread(runnable);
        }
    }

    static void joinArrays(Runnable[] runnables, int[] array) {
        if (runnables.length == 1) {
            return;
        }
        int startL = ((SortByShellRunnable) runnables[0]).getStartIndex();
        int endL = ((SortByShellRunnable) runnables[0]).getEndIndex();

        for(int i = 1; i< runnables.length; i++){
            int startR = ((SortByShellRunnable) runnables[i]).getStartIndex();
            int endR = ((SortByShellRunnable) runnables[i]).getEndIndex();
            _joinArrays(array, startL, endL, startR, endR);
            endL = endR;
        }
    }

    static void startThreads(Thread[] threads) {
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void _joinArrays(int[] array, int startL, int endL, int startR, int endR) {
        int l = startL;
        int r = startR;
        int countL = endL - startL + 1;
        int countR = endR - startR + 1;
        int[] arrayCopy = Arrays.copyOfRange(array, startL, endR+1);
        for (int i = l; i<=endR; i++){
            if (l == (startL + countL)) {
                for (; r < (startR + countR); r++, i++) {
                    array[i] = arrayCopy[r];
                }
                break;
            }
            if (r == (startR + countR)) {
                for (; l < (startL + countL); l++, i++) {
                    array[i] = arrayCopy[l];
                }
                break;
            }
            if (arrayCopy[l] <= arrayCopy[r]) {
                array[i] = arrayCopy[l];
                l++;
            }
            else{
                array[i] = arrayCopy[r];
                r++;
            }
        }
    }
}
