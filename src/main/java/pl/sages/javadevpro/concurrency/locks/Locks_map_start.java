package pl.sages.javadevpro.concurrency.locks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;

class Locks_map_start {

    public static void main(String[] args) throws InterruptedException {
        new Locks_map_start().invoke();
    }

    private void invoke() throws InterruptedException {
        MyConcurrentHashMap map = new MyConcurrentHashMap();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Thread t = new MyThread(map);
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }
        System.out.println(map);
    }

    static class MyThread extends Thread {
        private MyConcurrentHashMap map;

        public MyThread(MyConcurrentHashMap map) {
            this.map = map;
        }

        public void run() {
            for (int i = 0; i < 1000; i++) {
                // TODO: 1. inkrementuj wartość pod kluczem 'key'
                map.compute("key", (k, v) -> (v == null) ? 1 : v + 1);
            }
        }
    }

    static class MyConcurrentHashMap {
        Map<String, Integer> map = new HashMap<>();
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Lock read = readWriteLock.readLock();
        Lock write = readWriteLock.writeLock();

        public void compute(String key, BiFunction<String, Integer, Integer> function) {
            write.lock();
            try {
                map.compute(key, function);
            } finally {
                write.unlock();
            }
        }

        @Override
        public String toString() {
            read.lock();
            try {
                return map.toString();
            } finally {
                read.unlock();
            }
        }
    }
    // TODO: 2. Napisz adapter który uczyni HashMapę bezpieczną wątkowo. Użyj ReentrantLock
    // TODO: 3. Użyj ReadWriteReentrantLock by oddzielić operację czytania i zapisu
    // TODO: 4. Użyj StampedLock by czytać optimistic lockingiem.
}