package pl.sages.javadevpro.concurrency.locks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class ReadWriteLock_concurrentHashMap {

    public static void main(String[] args) throws InterruptedException {
        new ReadWriteLock_concurrentHashMap().invoke();
    }

    private void invoke() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

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
        private ConcurrentHashMap<String, Integer> map;

        public MyThread(ConcurrentHashMap<String, Integer> map) {
            this.map = map;
        }

        public void run() {
            for (int i = 0; i < 1000; i++) {
                map.compute("key", (k, v) -> (v == null) ? 1 : v + 1);
            }
        }
    }
}