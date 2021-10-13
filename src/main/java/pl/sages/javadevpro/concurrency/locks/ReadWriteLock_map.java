package pl.sages.javadevpro.concurrency.locks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiFunction;

class ReadWriteLock_map {

    public static void main(String[] args) throws InterruptedException {
        new ReadWriteLock_map().invoke();
    }

    private void invoke() throws InterruptedException {
        MyConcurrentMap map = new MyConcurrentMap();

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
        private MyConcurrentMap map;

        public MyThread(MyConcurrentMap map) {
            this.map = map;
        }

        public void run() {
            for (int i = 0; i < 1000; i++) {
                map.compute("key", (k, v) -> (v == null) ? 1 : v + 1);
            }
        }
    }


    private class MyConcurrentMap {
        Map<String, Integer> map = new HashMap<>();
        StampedLock lock = new StampedLock();

        public void compute(String key, BiFunction<String, Integer, Integer> function) {
            long stamp = lock.writeLock();
            try {
                map.compute(key, function);
            } finally {
                lock.unlockWrite(stamp);
            }
        }

        public Integer get(String key) {
            long stamp = lock.tryOptimisticRead();
            Integer value = map.get(key);
            if (lock.validate(stamp)) {
                return value;
            } else {
                long s = lock.readLock();
                try {
                    return map.get(key);
                } finally {
                    lock.unlockRead(s);
                }
            }
        }

//        @Override
//        public String toString() {
//
//            try {
//                return map.toString();
//            } finally {
//                lock.unlockRead(stamp);
//            }
//        }
    }
}