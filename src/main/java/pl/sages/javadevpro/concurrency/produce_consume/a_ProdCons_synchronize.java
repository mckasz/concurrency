package pl.sages.javadevpro.concurrency.produce_consume;

import java.util.Random;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;
import static pl.sages.javadevpro.concurrency.Util.threadWait;

public class a_ProdCons_synchronize {
    private static final Random r = new Random();
    public static final int NUMBER_OF_LOOPS = 20;

    public static void main(String[] args) throws InterruptedException {
        Storage storage = new Storage();
        Thread c = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_LOOPS; i++) {
                System.out.println("Konsument pobral " + storage.get());
                sleepFor(r.nextInt(100));
            }
        });
        Thread p = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_LOOPS; i++) {
                storage.put(i);
                System.out.println("Producent wyprodukowal " + i);
                sleepFor(r.nextInt(100));
            }
        });
        p.setName("Producent");
        c.setName("Konsument");
        p.start();
        c.start();
        p.join();
        c.join();
    }

    static class Storage {
        private int value;
        private volatile boolean hasValue = false;

        public synchronized int get() {
            while(!hasValue) {
                System.out.println(threadName() + " : brak wartości, czekam");
                threadWait(this);
            }
            notifyAll();
            hasValue = false;
            return value;
        }

        public synchronized void put(int value) {
            while (hasValue) {
                System.out.println(threadName() + " : nie ma miejsca, czekam");
                threadWait(this);
            }
            notifyAll();
            hasValue = true;
            this.value = value;
        }
    }
}
