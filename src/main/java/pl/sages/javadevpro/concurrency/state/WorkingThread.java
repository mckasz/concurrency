package pl.sages.javadevpro.concurrency.state;

class WorkingThread extends Thread {
    @Override
    public void run() {
        while (true) {
            if (isInterrupted()) {
                return;
            }
        }
    }
}
