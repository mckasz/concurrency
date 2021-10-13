package pl.sages.javadevpro.concurrency.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

class ForkJoinMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        MyWorkerThreadFactory factory = new MyWorkerThreadFactory();
        ForkJoinPool pool = new ForkJoinPool(10, factory, null, false);

        int array[] = new int[100_000];

        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }

        MyRecursiveTask task = new MyRecursiveTask(array, 0, array.length);
        pool.execute(task);

        task.join();
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("Main result: " + task.get());
    }
}
