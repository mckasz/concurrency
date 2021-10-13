package pl.sages.javadevpro.concurrency.executors;

import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;

class MyExecutorService {

    public static void main(String[] args) {
        MyExecutorService service = new MyExecutorService(2);
        service.addTask(new DummyTask(1));
        service.addTask(new DummyTask(2));
        service.addTask(new DummyTask(3));
        service.addTask(new DummyTask(4));
        service.addTask(new DummyTask(5));
        service.addTask(new DummyTask(6));
    }

    private final Deque<Runnable> tasks = new ConcurrentLinkedDeque<>();
    private final Map<MyThread, Boolean> busyThreads = new ConcurrentHashMap<>();

    public MyExecutorService(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            MyThread t = new MyThread(this);
            t.start();
            busyThreads.put(t, false);
        }
    }

    public void addTask(Runnable task) {
        tryToAssignTask(task);
    }

    private synchronized void taskFinished(MyThread myThread) {
        if (!tasks.isEmpty()) {
            Runnable task = tasks.poll();
            busyThreads.put(myThread, false);
            tryToAssignTask(task);
        }
    }

    private synchronized void tryToAssignTask(Runnable task) {
        Optional<MyThread> maybeThread = busyThreads.entrySet()
                                                    .stream()
                                                    .filter(e -> !e.getValue())
                                                    .map(Map.Entry::getKey)
                                                    .findFirst();
//        maybeThread.ifPresentOrElse(t -> {
//            busyThreads.put(t, true);
//            t.giveTask(task);
//        }, () -> tasks.push(task));
    }


    private static class MyThread extends Thread {

        private final MyExecutorService service;
        private volatile Runnable task;

        public MyThread(MyExecutorService service) {
            this.service = service;
        }

        public void giveTask(Runnable task) {
            this.task = task;
        }

        public void run() {
            while (true) {
                if (task != null) {
                    System.out.println(threadName() + " got task. Running");
                    task.run();
                    System.out.println(threadName() + " task finished.");
                    task = null;
                    service.taskFinished(this);
                } else {
                    sleepFor(100);
                }
            }
        }
    }

}
