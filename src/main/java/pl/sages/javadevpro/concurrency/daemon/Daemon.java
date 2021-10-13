package pl.sages.javadevpro.concurrency.daemon;

import pl.sages.javadevpro.concurrency.Util;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;

class Daemon {
    // TODO: jak zachowuje się aplikacja kiedy ma tylko wątek demonowy?
    public static void main(String[] args) {
        Thread t = new MyThread();
        t.setDaemon(false);
        t.start();
        sleepFor(100);
        System.out.println("Is Main a daemon? " + Thread.currentThread().isDaemon());
    }

    private static class MyThread extends Thread {
        public void run() {
            Util.sleepFor(1000_000);
            System.out.println("Daemon: I am but a shadow");
        }
    }
}
