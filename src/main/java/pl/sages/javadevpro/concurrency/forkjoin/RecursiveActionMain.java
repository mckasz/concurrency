package pl.sages.javadevpro.concurrency.forkjoin;

import pl.sages.javadevpro.concurrency.locks.LongText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class RecursiveActionMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<String> submit = pool.submit(new UppercaseRecursiveTask(LongText.TEXT));
        String result = submit.get();

        System.out.println(result);
        pool.shutdown();
        Collections.synchronizedList()
        pool.awaitTermination(1, TimeUnit.DAYS);
    }

    static class UppercaseRecursiveTask extends RecursiveTask<String> {

        private String workload = "";
        private static final int THRESHOLD = 4;

        public UppercaseRecursiveTask(String workload) {
            this.workload = workload;
        }

        @Override
        protected String compute() {
            if (workload.length() > THRESHOLD) {
                return ForkJoinTask.invokeAll(createSubtasks())
                                   .stream()
                                   .map(ForkJoinTask::join)
                                   .reduce("", String::concat);
            } else {
                return processing(workload);
            }
        }

        private List<UppercaseRecursiveTask> createSubtasks() {
            List<UppercaseRecursiveTask> subtasks = new ArrayList<>();

            String partOne = workload.substring(0, workload.length() / 2);
            String partTwo = workload.substring(workload.length() / 2);

            subtasks.add(new UppercaseRecursiveTask(partOne));
            subtasks.add(new UppercaseRecursiveTask(partTwo));

            return subtasks;
        }

        private String processing(String work) {
            String result = work.toUpperCase();
            System.out.println("This result - (" + result + ") - was processed by " + Thread.currentThread().getName());
            return result;
        }
    }

}
