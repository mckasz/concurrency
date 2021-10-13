package pl.sages.javadevpro.concurrency.interruption;


import java.math.BigInteger;
import java.util.Random;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;

class Interruption {
    final static Object monitor = new Object();

    public static void main(String[] args) {
        Thread t1 = new MyThread();
        t1.setName("T1");
        t1.start();

        sleepFor(100);
        Thread t2 = new MyThread();
        t2.setName("T2");
        t2.start();
        sleepFor(2000);
        for (int i = 0; i < 2; i++) {
            System.out.println("Interrupting, state: " + t2.getState());
            t2.interrupt();
            sleepFor(2000);
        }
        t1.interrupt();
    }


    private static class MyThread extends Thread {

        public void run() {
            super.stop();
            try {
                System.out.println(threadName() + " staring work. Acquiring lock");
                synchronized (monitor) {
                    while (true) {
                        System.out.println(threadName() + " is working. Interrupt status: " + isInterrupted());
                        BigInteger veryBig = new BigInteger(2000, new Random());
                        veryBig.nextProbablePrime();
                        System.out.println("part of job done ");
                        if (isInterrupted()) {
                            System.out.println(threadName() + " Interrupted. Interrupt status: " + isInterrupted());
                            throw new InterruptedException();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(threadName() + " Interrupt status: " + isInterrupted());
                System.out.println(threadName() + " Interrupted. Finishing work");
            }
        }
    }
}
