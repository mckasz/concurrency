package pl.sages.javadevpro.concurrency.state;

class State {

    public static void main(String[] args) throws InterruptedException {
        Thread workingThread = new Thread(new WorkingThread());
        System.out.println("Working state: " + workingThread.getState() + "\n");
        workingThread.start();
        Thread.sleep(10);
        System.out.println("Working state: " + workingThread.getState() + "\n");

        Thread waitingThread = new Thread(new WaitingThread());
        waitingThread.start();
        Thread.sleep(10);
        System.out.println("Waiting state: " + waitingThread.getState() + "\n");

        Thread timedWaitingThread = new Thread(new TimedWaitingThread());
        timedWaitingThread.start();
        Thread.sleep(10);
        System.out.println("Timed waiting state: " + timedWaitingThread.getState() + "\n");

        Thread blockedThread = new BlockedThread();
        blockedThread.start();
        Thread.sleep(10);
        System.out.println("Blocked state: " + blockedThread.getState() + "\n");

        Thread terminatedThread = new TerminatedThread();
        terminatedThread.start();
        Thread.sleep(10);
        System.out.println("Terminated state: " + terminatedThread.getState() + "\n");

        workingThread.interrupt();
        waitingThread.interrupt();
        timedWaitingThread.interrupt();
        blockedThread.interrupt();
        terminatedThread.interrupt();
    }
}
