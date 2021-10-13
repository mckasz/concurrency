package pl.sages.javadevpro.concurrency.produce_consume;

import java.util.Random;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;


// TODO: Zadanie na producenta i konsumenta. Na tym etapie zrobione bardzo nieefektywnie.
// 1. naprawić by działało
// 2. upewnić się że działa dla wielu wątków producenta i konsumenta
// 3. użyć locków
// 4. pojedyncze pole na kolejkę, czyli uzyskać coś na kształt ConcurrentLinkedQueue
public class ProdCons {
    private static final Random r = new Random();
    public static final int NUMBER_OF_LOOPS = 200;

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

        public int get() {
            while (!hasValue) {
            }
            hasValue = false;
            return value;
        }

        public void put(int value) {
            while (hasValue) {
            }
            hasValue = true;
            this.value = value;
        }
    }
}
