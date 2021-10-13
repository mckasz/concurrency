package pl.sages.javadevpro.concurrency.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

class MyRecursiveTask extends RecursiveTask<Integer> {
    private int array[];
    private int start, end;

    public MyRecursiveTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        Integer result;
        MyWorkerThread thread = (MyWorkerThread) Thread.currentThread();
        thread.addTask();

        if (end - start > 100) {
            int mid = (start + end) / 2;

            MyRecursiveTask t1 = new MyRecursiveTask(array, start, mid);
            MyRecursiveTask t2 = new MyRecursiveTask(array, mid, end);
            result = addResult(t1, t2);
        } else {
            int add = 0;
            for (int i = start; i < end; i++) {
                add += array[i];
            }
            result = add;
        }

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Integer addResult(MyRecursiveTask t1, MyRecursiveTask t2) {
        int value;
        try {
            value = t1.get() + t2.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            value = 0;
        }
        return value;
    }
}
