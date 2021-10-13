package pl.sages.javadevpro.concurrency.interruption;

import java.math.BigInteger;
import java.util.Random;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;

class Interruption_start {
    final static Object monitor = new Object();

    public static void main(String[] args) {
        // TODO  3. odkomentuj pierwszy wątek i sprawdź jak zachowa się program. Na koniec spraw by oba wątki zostały przerwane
//        Thread t1 = new MyThread();
//        t1.setName("T1");
//        t1.start();

        sleepFor(100);
        Thread t2 = new MyThread();
        t2.setName("T2");
        t2.start();
        sleepFor(2000);
        for (int i = 0; i < 2; i++) {
            System.out.println("Interrupting");
            t2.interrupt();
            sleepFor(2000);
        }
    }

    // TODO: 1. dodaj obsługę przerwania
    // TODO: 2. zobacz co się stanie kiedy użyjemy metody sleepFor
    private static class MyThread extends Thread {
        public void run() {
            System.out.println(threadName() + " staring work. Acquiring lock");
            synchronized (monitor) {
                while (true) {
                    BigInteger veryBig = new BigInteger(2000, new Random());
                    veryBig.nextProbablePrime();
                    System.out.println("part of job done ");
                }
            }
        }
    }
}
