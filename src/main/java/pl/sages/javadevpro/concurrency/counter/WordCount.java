package pl.sages.javadevpro.concurrency.counter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

class WordCount {

    private static final String TEXT = "Price\n" +
            "Like Tesla, Rivian is aiming for the luxury end of the market. The base Explore model starts at $67,500, while a fancier Adventure trim will run you $73,000. A bigger battery pack is a $10,000 add-on. \n" +
            "\n" +
            "The F-150 Lightning starts at around $40,000 for a basic work truck (the Pro trim). However, like other F-150s, the Lightning can get considerably fancier and more expensive when you start looking at options and higher trim levels. \n" +
            "\n" +
            "A fully loaded Lightning will cost around $90,000, roughly the same as an R1T will all the bells and whistles. \n" +
            "\n" +
            "Size\n" +
            "The F-150 Lightning is the bigger truck of the two. It's 232.7 inches long, compared with the R1T's 217.1 inches. They're about the same width with the mirrors folded in. \n" +
            "\n" +
            "Ford F-150 Lightning with a trailer\n" +
            "Ford F-150 Lightning. Ford\n" +
            "Much of that extra length comes by way of the Lightning's bed, which measures 5.5 feet. The R1T's bed is a foot shorter, but it's meant more for hauling camping equipment than lumber. \n" +
            "\n" +
            "Performance and Capability\n" +
            "Both trucks offer silent, forceful acceleration and excellent handling thanks to powerful electric motors and a low center of gravity. \n" +
            "\n" +
            "In terms of the numbers, the R1T promises more than 800 horsepower and over 900 pound-feet of torque from its four motors — one at each wheel. The F-150 Lightning's pair of electric motors put out 563 horsepower and 775 pound-feet of torque when mated to the larger battery pack, Ford says. \n" +
            "\n";


    Map<String, Integer> map = new TreeMap<>();
    public static void main(String[] args) throws InterruptedException {
        new WordCount().invoke();
    }

    private void invoke() {
        // TODO: zlecić wykonanie liczenia słów osobnemu wątkowi i poczekać na wynik
        countWords();

        printResults();
    }

    private void countWords() {
        List<String> lines = Arrays.asList(TEXT.split("\n"));
        for (String line : lines) {
            List<String> words = Arrays.asList(TEXT.split("\n"));
            for (String word : words) {
                map.compute(word, (k, v) -> v == null ? 1 : v + 1);
            }
        }
    }

    private void printResults() {
        List<String> result = map.entrySet()
                                  .stream()
                                  .sorted(Map.Entry.comparingByValue())
                                  .map(e -> e.getKey() + ": " + e.getValue())
                                  .collect(Collectors.toList());
        Collections.reverse(result);
        for (String s : result) {
            System.out.println(s);
        }
    }
}
