package pl.sages.javadevpro.concurrency.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class TaskFlood {

    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        while (true) {
            fixedThreadPool.submit(new DummyTask(1));
        }
    }
}
