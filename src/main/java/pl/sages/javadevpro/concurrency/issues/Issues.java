package pl.sages.javadevpro.concurrency.issues;

import java.util.concurrent.CyclicBarrier;

import static pl.sages.javadevpro.concurrency.Util.await;
import static pl.sages.javadevpro.concurrency.Util.threadName;

class Issues {

    private final Object monitor = new Object();
    private final CyclicBarrier gate = new CyclicBarrier(4);


    public static void main(String[] args) {
        new Issues().invoke();
    }

    private void invoke() {
        Thread t1 = new MyThread(gate, monitor);
        t1.setPriority(1);
        t1.setName("Thread #X: priority: " +  t1.getPriority());
        t1.start();

        for (int i = 0; i < 2; i++) {
            Thread t = new MyThread(gate, monitor);
            t.setPriority(10);
            t.setName("Thread #" + i + ": priority: " +  t.getPriority());
            t.start();
        }

        await(gate);
        System.out.println("all threads started");
    }

    static class MyThread extends Thread {
        private final CyclicBarrier gate;
        private final Object monitor;
        private int cnt;

        public MyThread(CyclicBarrier gate, Object monitor) {
            this.gate = gate;
            this.monitor = monitor;
        }

        public void run() {
            await(gate);
            while (true) {
                synchronized (monitor) {
                    System.out.println(threadName() + " count started");
                    for (int i = 0; i < 1_000_000; i++) {
                        cnt++;
                    }
                    System.out.println(threadName() + " counting finished. Result " + cnt);
                    monitor.notifyAll();
                    cnt = 0;
                }
            }
        }
    }
}
