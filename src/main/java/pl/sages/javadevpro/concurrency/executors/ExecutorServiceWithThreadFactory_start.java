package pl.sages.javadevpro.concurrency.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

class ExecutorServiceWithThreadFactory_start {

    public static void main(String[] args) throws Exception {
        new ExecutorServiceWithThreadFactory_start().invoke();
    }

    private void invoke() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5, new MyThreadFactory());

        for (int i = 0; i < 20; i++) {
            executorService.submit(new DummyTask(i));
        }

    }
    // TODO: Stwórz fabrykę, która nazywa watki z odpowiednim prefixem, numeruje je i ustawia jako demony
    private static class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            return null;
        }
    }
}
