package pl.sages.javadevpro.concurrency.produce_consume;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static pl.sages.javadevpro.concurrency.Util.awaitUntil;
import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;

public class c_ProdCons_queue {
    private static final Random r = new Random();
    public static final int NUMBER_OF_LOOPS = 20;

    public static void main(String[] args) throws InterruptedException {
        Storage storage = new Storage(10);
        Thread p = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_LOOPS; i++) {
                storage.put(i);
                System.out.println("Producent wyprodukowal " + i);
                sleepFor(r.nextInt(100));
            }
        });
        Thread c = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_LOOPS; i++) {
                System.out.println("Konsument pobral " + storage.get());
                sleepFor(r.nextInt(10));
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
        private final Queue<Integer> queue = new LinkedList<>();
        private final int capacity;

        private final Lock aLock = new ReentrantLock();
        private final Condition storageFree = aLock.newCondition();
        private final Condition storageNotEmpty = aLock.newCondition();

        public Storage(int capacity) {
            this.capacity = capacity;
        }

        public Integer get() {
            aLock.lock();
            try {
                while (queue.isEmpty()) {
                    System.out.println(threadName() + " : brak wartości, czekam");
                    awaitUntil(storageNotEmpty);
                }
                storageFree.signalAll();
                return queue.poll();
            } finally {
                aLock.unlock();
            }
        }

        public void put(int value) {
            aLock.lock();
            try {
                while (queue.size() == capacity) {
                    System.out.println(threadName() + " : nie ma miejsca, czekam");
                    awaitUntil(storageFree);
                }
                storageNotEmpty.signalAll();
                queue.offer(value);
            } finally {
                aLock.unlock();
            }
        }
    }

}
