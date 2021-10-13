package pl.sages.javadevpro.concurrency.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReentrantLockSample {
    ReentrantLock lock = new ReentrantLock();
    int counter = 0;

    public void perform() {
        lock.lock(); // hold count = 1
        try {
            // sekcja krytyczna
            performSomethingElse();
            counter++;
        } finally {
            lock.unlock();  // hold count = 0
        }
    }

    public void performSomethingElse() {
        lock.lock();  // hold count = 2
        try {
            // sekcja krytyczna
        } finally {
            lock.unlock();  // hold count = 1
        }
    }

    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    Lock read = readWriteLock.readLock();
    Lock write = readWriteLock.writeLock();



}
