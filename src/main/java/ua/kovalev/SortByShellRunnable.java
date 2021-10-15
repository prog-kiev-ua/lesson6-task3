package ua.kovalev;

public class SortByShellRunnable implements Runnable {
    private int startIndex;
    private int endIndex;
    private int[] array;

    public SortByShellRunnable() {
        super();
    }

    public SortByShellRunnable(int[] array, int startIndex, int endIndex) {
        super();
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.array = array;
    }

    @Override
    public void run() {
        int step = ((endIndex - startIndex) + 1) / 2;
        for (; step > 0; ) {
            for (int i = startIndex + step; i <= endIndex; i++) {
                for (int j = i; (j - step >= startIndex) && array[j] < array[j - step]; j -= step) {
                    int temp = array[j];
                    array[j] = array[j - step];
                    array[j - step] = temp;
                }
            }
            step = step / 2;
        }
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    @Override
    public String toString() {
        return "SortByShellRunnable{" +
                "startIndex=" + startIndex +
                ", endIndex=" + endIndex +
                '}';
    }
}
