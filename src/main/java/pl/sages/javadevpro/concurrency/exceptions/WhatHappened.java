package pl.sages.javadevpro.concurrency.exceptions;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;

class WhatHappened {

    // TODO: Co dzieje się, kiedy wątek rzuca wyjątek? Co stanie się na serwerze aplikacyjnym?
    public static void main(String[] args) {
        Thread t = new MyThread();
        t.start();
        sleepFor(100);
        System.out.println(t.getState());
    }

    private static class MyThread extends Thread {

        public void run() {
            throw new RuntimeException("Bum!");
        }
    }
}
