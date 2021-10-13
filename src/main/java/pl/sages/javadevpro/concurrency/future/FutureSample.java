package pl.sages.javadevpro.concurrency.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class FutureSampleTask implements Callable<String> {
    @Override
    public String call() {
        return "solution";
    }
}

public class FutureSample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(1);

        FutureSampleTask task = new FutureSampleTask();
        Future<String> future = pool.submit(task);

        while (!future.isDone()) {
            System.out.println("FutureSampleTask is not completed...");
            Thread.sleep(1);
        }

        System.out.println("FutureSampleTask is completed");
        String solution = future.get();
        System.out.println(solution);


        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);
    }
}
