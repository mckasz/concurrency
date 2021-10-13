package pl.sages.javadevpro.concurrency.atomic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class b_Atomics_CAS {
    public static final int NUM_OF_THREADS = 10;
    public static final int CNT_MAX = 10_000_000;
    public static final int EXPECTED_RESULT = CNT_MAX * NUM_OF_THREADS;

    public static void main(String[] args) throws Exception {
        Counter c = new Counter();
        long start = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < NUM_OF_THREADS; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < CNT_MAX; j++) {
                    c.increment();
                }
                System.out.println(c.get());
            });
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Finished. Task took " + (System.currentTimeMillis() - start) + "ms");
        if (c.get() == EXPECTED_RESULT) {
            System.out.println("Perfect!");
        } else {
            System.out.println("Buuu! Expected: " + EXPECTED_RESULT + " but got " + c.get());
        }
    }

    static class Counter {
        private final AtomicInteger c = new AtomicInteger();

        public void increment() {
            while (true) {
                int existingValue = c.get();
                int newValue = existingValue + 1;
                if (c.compareAndSet(existingValue, newValue)) {
                    return;
                }
            }
        }

        public int get() {
            return c.get();
        }
    }
}
