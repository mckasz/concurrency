package pl.sages.javadevpro.concurrency.issues;

import java.util.Deque;
import java.util.LinkedList;

import static pl.sages.javadevpro.concurrency.Util.sleepFor;

class Livelock {
    public static void main(String[] args) {
        new Livelock().invoke();
    }

    private void invoke() {
        Deque<String> queue = new LinkedList<>();
        queue.add("Task 1");
        queue.add("Task 2");
        queue.add(null);
        queue.add("Task 4");

        Thread t = new MyThread(queue);
        t.start();
    }

    private static class MyThread extends Thread {
        private final Deque<String> queue;

        public MyThread(Deque<String> queue) {
            this.queue = queue;
        }

        public void run() {
            while (!queue.isEmpty()) {
                String taskName = queue.poll();
                try {
                    sleepFor(1000);
                    System.out.println("I am doing task " + taskName.toUpperCase());
                } catch (Exception e) {
                    System.out.println("Oh no! Exception happened!\n" + e);
                    queue.push(taskName);
                }
            }
        }
    }
}
