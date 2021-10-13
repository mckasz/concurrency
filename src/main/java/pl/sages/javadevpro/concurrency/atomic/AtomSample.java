package pl.sages.javadevpro.concurrency.atomic;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomSample {

    public static void main(String[] args) {
        Counter c = new Counter();

        Executor e = Executors.newFixedThreadPool(10);
        e.execute(() -> {
            for (int j = 0; j < 2000; j++) {
                c.increment();
            }
            System.out.println(c.get());
        });

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(() -> {

            });
            t.start();
        }

        try {  Thread.sleep(100);   }  catch (InterruptedException ea)  {  }
        System.out.println(c.get());
    }

    // TODO 1. popraw kod tak by licznik wskazywał poprawną wartość, użyj klasy Atomic
    // TODO 2. użyj metody compare and set
    // TODO 3. zrób tak by poczekać na wynik wykonania pracy wszystkich wątków
    // TODO 4. zmierz i porównaj czasy wykonania przy różnych metodach synchronizacji.

    static class Counter {
        private AtomicInteger c = new AtomicInteger();

        public void increment() {
            while (true) {
                int existingValue = c.get();
                int newValue = existingValue + 1;
                if(c.compareAndSet(existingValue, newValue)) {
                    return;
                }
            }
        }

        public int get() {
            return c.get();
        }
    }
}
