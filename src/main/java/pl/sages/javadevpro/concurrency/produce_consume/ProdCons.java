package pl.sages.javadevpro.concurrency.produce_consume;

import java.util.Random;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;

public class ProdCons {
    private static final Random r = new Random();
    public static final int NUMBER_OF_LOOPS = 20;

    public static void main(String[] args) throws InterruptedException {
        a_ProdCons_synchronize.Storage storage = new a_ProdCons_synchronize.Storage();
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
            while (hasValue == false) {
            }
            hasValue = false;
            return value;
        }

        public void put(int value) {
            while (hasValue == true) {
            }
            hasValue = true;
            this.value = value;
        }
    }
}
