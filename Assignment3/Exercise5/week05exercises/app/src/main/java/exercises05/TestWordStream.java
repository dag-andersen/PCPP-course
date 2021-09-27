// Week 3
// sestoft@itu.dk * 2015-09-09
package exercises05;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestWordStream {
  public static void main(String[] args) {
    String filename = "src/main/resources/english-words.txt";
    // TestWordStream.letters("Persistent").forEach((key, action) ->
    // System.out.println(key + " " + action));
    readWords10(filename);
  }

  public static Stream<String> readWords(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      return reader.lines().map(x -> x.trim()).filter(x -> x.length() > 0);
    } catch (IOException exn) {
      return Stream.<String>empty();
    }
  }

  public static Stream<String> readWords2(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      return reader.lines().map(x -> x.trim()).filter(x -> x.length() > 0).limit(100);
    } catch (IOException exn) {
      return Stream.<String>empty();
    }
  }

  public static void readWords3(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      reader.lines().map(x -> x.trim()).filter(x -> x.length() > 22).forEach(System.out::println);
    } catch (IOException exn) {
    }
  }

  public static void readWords4(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      reader.lines().map(x -> x.trim()).filter(x -> x.length() > 22).findFirst()
          .ifPresent(action -> System.out.println(action));
    } catch (IOException exn) {
    }
  }

  public static void readWords5(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      reader.lines().map(x -> x.trim()).filter(x -> x.length() > 0 && isPalindrome(x))
          .forEach(action -> System.out.println(action));
    } catch (IOException exn) {
    }
  }

  public static void readWords6(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      reader.lines().parallel().map(x -> x.trim()).filter(x -> x.length() > 0 && isPalindrome(x))
          .forEach(action -> System.out.println(action));
    } catch (IOException exn) {
    }
  }

  // As usual it is really difficult to reason about performance on such small
  // datasets, since the time it takes to create a thread overshadows the time it
  // takes to iterate over the collection

  public static boolean isPalindrome(String s) {
    return s.equals(new StringBuilder(s).reverse().toString());
  }

  public static void readWords7(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      var stats = reader.lines().parallel().map(x -> x.trim()).mapToInt(x -> x.length()).summaryStatistics();
      System.out.println("Min: " + stats.getMin());
      System.out.println("Max: " + stats.getMax());
      System.out.println("Average: " + stats.getAverage());
    } catch (IOException exn) {
    }
  }

  public static void readWords8(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      reader.lines().parallel().map(x -> x.trim()).filter(x -> x.length() > 0)
          .collect(Collectors.groupingBy(x -> x.length()))
          .forEach((key, value) -> System.out.println(key + " " + value.size()));
    } catch (IOException exn) {
    }
  }

  public static void readWords9(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      reader.lines().parallel().map(x -> x.trim()).limit(100).filter(x -> x.length() > 0).forEach(value -> {
        System.out.println(value);
        TestWordStream.letters(value).forEach((key, action) -> System.out.println(key + " " + action));
        System.out.println();
      });
    } catch (IOException exn) {
    }
  }

  public static void readWords10(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      var sum = reader.lines().parallel().map(x -> x.trim()).filter(x -> x.length() > 0)
          .flatMapToInt(CharSequence::chars).mapToObj(i -> (char) i).filter(c -> Character.toLowerCase(c) == 'e')
          .mapToInt(x -> 1).sum();
      System.out.println(sum);
    } catch (IOException exn) {
    }
  }

  public static Map<Character, Integer> letters(String s) {
    Map<Character, Integer> res = new TreeMap<>();
    for (int i = 0; i < s.length(); i++) {
      char c = Character.toLowerCase(s.charAt(i));
      res.merge(c, 1, Integer::sum);
    }
    return res;
  }
}
