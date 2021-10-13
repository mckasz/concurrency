package pl.sages.javadevpro.concurrency.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class TaskFlood {

    public static void main(String[] args) {
        // TODO: można włączyć poczekać chwilę by aplikacja podziałała trochę i zobaczyć
        //  jak wygląda na jvisualvm. Uwaga: komputer może mocno zamulać.
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        while (true) {
            fixedThreadPool.submit(new DummyTask(1));
        }
    }
}
