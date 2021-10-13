package pl.sages.javadevpro.concurrency.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Locks {

    // ========== Classic synchronization =============

    public static final Object o = new Object();

    public void call() {
        synchronized (o) {
            // api call
        }
    }
    public void receive() {
        synchronized (o) {
            // callback for receive
        }
    }

    // ================== Lock usage ==================


    private static final Lock lock = new ReentrantLock();

    public void lockCall() {
        lock.lock();
        // api call
    }
    public void lockReceive() {
        lock.unlock();
        // callback
    }

    public void tryLockMethod() throws InterruptedException {
        boolean lockAcquired = lock.tryLock();
        if (lockAcquired) {
            try {
                // do job
            } finally {
                lock.unlock();
            }
        } else {
            // nie dostałem locka, zrobię coś innego
        }
    }

    public void lockInterruptiblySample() throws InterruptedException {
        lock.lockInterruptibly(); // wątek może zostać Interruptowany, kiecy czeka na locka,
        try {
            // do job
        } finally {
            lock.unlock();
        }
    }
}