package pl.sages.javadevpro.concurrency.issues;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;
import static pl.sages.javadevpro.concurrency.Util.threadName;

class Deadlock {

    private final Object monitor1 = new MyMonitor(1);
    private final Object monitor2 = new MyMonitor(2);

    public static void main(String[] args) {
        new Deadlock().invoke();
    }

    private void invoke() {
        Thread t1 = new ThreadBlockingOn(monitor1, monitor2);
        Thread t2 = new ThreadBlockingOn(monitor2, monitor1);

        t1.start();
        t2.start();
    }

    private static class ThreadBlockingOn extends Thread {
        private final Object o1;
        private final Object o2;

        public ThreadBlockingOn(Object o1, Object o2) {
            this.o1 = o1;
            this.o2 = o2;
        }

        public void run() {
            System.out.println(threadName() + " will acquire lock on " + o1);
            synchronized (o1) {
                System.out.println(threadName() + " " + o1 + " acquired");
                sleepFor(100);
                System.out.println(threadName() + " will acquire lock on " + o2);
                synchronized (o2) {
                    System.out.println(threadName() + " " + o2 + " acquired");
                    System.out.println(threadName() + " Doing something useful");
                }
            }
        }
    }

    private static class MyMonitor {
        private int i;

        public MyMonitor(int i) {
            this.i = i;
        }

        public String toString() {
            return "Monitor " + i;
        }
    }
}
