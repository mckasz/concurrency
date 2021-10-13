package pl.sages.javadevpro.concurrency.state;

class TimedWaitingThread extends Thread {
    @Override
    public void run() {
        try {
            Thread.sleep(1_000_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
