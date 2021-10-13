package pl.sages.javadevpro.concurrency.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class CompletableFutureSample {
    public static void main(String[] args) throws Exception {
        new CompletableFutureSample().invoke();
    }

    private void invoke() throws Exception {
        runAsync();
        chainResults();
        consumeResult();
        ignoreResult();
        composeFutures();
        combineFutures();
        acceptBoth();
    }

    private void runAsync() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> System.out.println("Hello World"));// po prostu wykonaj coś w innym wątku (bez zwracania wartości)
        future.get();
    }

    private void chainResults() throws Exception {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = stringCompletableFuture
                                                            .thenApply(s -> s + " World"); // Mamy wynik i dodajemy do niego wartość
        System.out.println(future.get());
    }

    private void consumeResult() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
                                                          .thenAccept(s -> System.out.println("Computation returned: " + s));
        future.get();
    }

    private void ignoreResult() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
                                                          .thenRun(() -> System.out.println("Computation finished."));
        future.get();
    }

    private void composeFutures() throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                                                                       // mamy wynik i korzystamy z niego w
                                                                       // rezultacie wykonania kolejnego future'a
                                                                       .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
        System.out.println(completableFuture.get());
    }

    private void combineFutures() throws Exception {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                                                                       // mamy wynik i korzystamy z niego w
                                                                       // rezultacie wykonania kolejnego future'a ale obie wartości są niezależne
                                                                       .thenCombine(CompletableFuture.supplyAsync(() -> " World"),
                                                                                    (s1, s2) -> s1 + s2);

        System.out.println(completableFuture.get());
    }

    private void acceptBoth() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                                                                         .thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"),
                                                                                         (s1, s2) -> System.out.println(
                                                                                                 s1 + s2));
        voidCompletableFuture.get();
    }
}
