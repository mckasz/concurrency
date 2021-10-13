package pl.sages.javadevpro.concurrency.executors;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class VerboseExecutorService extends ThreadPoolExecutor {
    // Dodaj informacje o
    // - liczbie wykonanych, trwających i czekających zadań w metodzie shutdown i shutdownNow
    // - liczbie wykonanych, trwających i czekających zadań w metodzie shutdown
    // - nazwie wątku. który będzie wykonywać zadanie
    // - rezultacie i czasie trwania zadania

    public VerboseExecutorService(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  @NotNull TimeUnit unit,
                                  @NotNull BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
}
