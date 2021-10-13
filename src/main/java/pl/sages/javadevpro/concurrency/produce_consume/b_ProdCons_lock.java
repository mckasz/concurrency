package pl.sages.javadevpro.concurrency.produce_consume;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static pl.sages.javadevpro.concurrency.Util.awaitUntil;
import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;

public class b_ProdCons_lock {
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
        c.start();
        p.start();
        c.join();
        p.join();
    }

    static class Storage {
        private Integer value;

        private final Lock aLock = new ReentrantLock();
        private final Condition storageFree = aLock.newCondition();
        private final Condition storageNotEmpty = aLock.newCondition();

        public int get() {
            aLock.lock();
            try {
                while (this.value == null) {
                    System.out.println(threadName() + " : brak wartości, czekam");
                    awaitUntil(storageNotEmpty);
                }
                Integer valueCopy = value;
                value = null;
                storageFree.signalAll();
                return valueCopy;
            } finally {
                aLock.unlock();
            }
        }

        public void put(int value) {
            aLock.lock();
            try {
                while (this.value != null) {
                    System.out.println(threadName() + " : nie ma miejsca, czekam");
                    awaitUntil(storageFree);
                }
                storageNotEmpty.signalAll();
                this.value = value;
            } finally {
                aLock.unlock();
            }
        }
    }
}
