package pl.sages.javadevpro.concurrency.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

class ExecutorServiceWithThreadFactory {

    public static void main(String[] args) throws Exception {
        new ExecutorServiceWithThreadFactory().invoke();
    }

    private void invoke() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5, new MyThreadFactory());

        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            futures.add(executorService.submit(new DummyTask(i)));
        }

        System.out.println("Calling shutdown");
        executorService.shutdown();
        System.out.println("Waiting on get");
        for (Future future : futures) {
            future.get();
        }
    }



    private static class MyThreadFactory implements ThreadFactory {
        AtomicInteger threadCount = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            int cnt = threadCount.getAndIncrement();
            thread.setName("My thread #" + cnt);
            thread.setDaemon(true);
            return thread;
        }
    }
}
