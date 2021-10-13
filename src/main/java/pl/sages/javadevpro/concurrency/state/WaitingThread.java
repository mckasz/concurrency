package pl.sages.javadevpro.concurrency.state;

class WaitingThread extends Thread {

    Thread sleepingThread = new TimedWaitingThread();

    @Override
    public void run() {
        sleepingThread.start();
        try {
            sleepingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
