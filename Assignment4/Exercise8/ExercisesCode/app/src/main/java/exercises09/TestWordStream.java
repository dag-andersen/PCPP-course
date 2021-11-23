// Week 3
// sestoft@itu.dk * 2015-09-09
package exercises09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class TestWordStream {
  static BufferedReader reader;

  public static void main(String[] args) {
    // TestWordStream.letters("Persistent").forEach((key, action) ->
    // System.out.println(key + " " + action));
    // readWords10(filename);

    String filename = "src/main/java/exercises09/english-words.txt";

    try {
      reader = new BufferedReader(new FileReader(filename));
    } catch (IOException exn) {
      System.out.println(exn);
    }

    readWordsRx.filter(line -> line.length() >= 22).take(100).subscribe((String v) -> {
      System.out.println(v);
    });

    try {
      reader.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public static Observable<String> readWordsRx = Observable.create(new ObservableOnSubscribe<String>() {
    @Override
    public void subscribe(ObservableEmitter<String> e) throws Exception {
      //reader.lines().limit(100).forEach(line -> e.onNext(line));
      reader.lines().forEach(line -> e.onNext(line));
    }
  });

  /* OLD TRASH */

  public static Stream<String> readWords(String filename) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filename));
      return reader.lines().map(x -> x.trim()).filter(x -> x.length() > 0);
    } catch (IOException exn) {
      return Stream.<String>empty();
    }
  }
}
