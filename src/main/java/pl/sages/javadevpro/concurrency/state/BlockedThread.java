package pl.sages.javadevpro.concurrency.state;

class BlockedThread extends Thread {

    private Object object = new Object();

    public BlockedThread() {
        Thread sleepingThread = new Thread(() -> {
            test();
//            synchronized (object) { // sleeping thread ma monitor na obiekcie object
//                try {
//                    Thread.sleep(1_000_000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        });
        sleepingThread.start();
    }

    @Override
    public void run() {
        test();
        synchronized (object) { //
//             do something
            System.out.println("It will take a while to get here :/");
        }
    }

    public synchronized void test() {
        try {
            Thread.sleep(1_000_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void test2() {
        try {
            Thread.sleep(1_000_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void test3() {
        try {
            Thread.sleep(1_000_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
