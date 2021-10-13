package pl.sages.javadevpro.concurrency.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class a_Atomics_simple {

    public static void main(String[] args) throws Exception {
        Counter c = new Counter();
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {
                for (int j = 0; j < 10_000; j++) {
                    c.increment();
                }
                System.out.println(c.get());
            });
            t.start();
        }

        try {  Thread.sleep(3000);   }  catch (InterruptedException e)  {  }
        System.out.println(c.get());
    }

    static class Counter {
        private final AtomicInteger c = new AtomicInteger();

        public void increment() {
            c.incrementAndGet();
        }

        public int get() {
            return c.get();
        }
    }

}
