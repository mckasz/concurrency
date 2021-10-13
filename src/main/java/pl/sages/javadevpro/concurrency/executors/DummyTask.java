package pl.sages.javadevpro.concurrency.executors;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;

class DummyTask implements Runnable {
    private final int i;

    public DummyTask(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        System.out.println(threadName() + " Running task " + i);
        sleepFor(1000);
        System.out.println(threadName() + " Task " + i + " finished");
    }

    @Override
    public String toString() {
        return "Task #" + i;
    }
}
