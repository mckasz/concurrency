package pl.sages.javadevpro.concurrency.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ExecutorServiceSample {

    public static void main(String[] args) {
        new ExecutorServiceSample().invoke();
    }

    private void invoke() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        ExecutorService fixedThreadPool =      Executors.newFixedThreadPool(10);
        ExecutorService workStealingPool =     Executors.newWorkStealingPool();
        ExecutorService cachedThreadPool =     Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
        scheduledExecutorService.scheduleWithFixedDelay(new DummyTask(1), 2, 1, TimeUnit.HOURS);
        scheduledExecutorService.schedule(new DummyTask(1), 1, TimeUnit.DAYS);


        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Future<?> submit = cachedThreadPool.submit(new DummyTask(i));
            futures.add(submit);
        }

//        for (Future future : futures) {
//            future.get();
//        }

        System.out.println("Calling shutdown");
        cachedThreadPool.shutdown();
        try {
            if (!cachedThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                cachedThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            cachedThreadPool.shutdownNow();
        }
    }


}
