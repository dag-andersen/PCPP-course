package exercises09;

import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/* This example is inspired by the StopWatch app in Head First. Android Development
http://shop.oreilly.com/product/0636920029045.do
Modified to Java, October 2020 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class StopwatchRx {
  public static void main(String[] args) {
    new StopwatchRx();
  }

  private StopwatchUIRx myUI;

  public StopwatchRx() {
    JFrame f = new JFrame("Stopwatch");
    f.setBounds(0, 0, 220, 220);
    myUI = new StopwatchUIRx(0, f);

    timer.filter(val -> myUI.running()).subscribe(display);

    rxPushStart.subscribe(displaySetRunningTrue);
    rxPushStop.subscribe(displaySetRunningFalse);
    rxPushReset.subscribe(displaySetAllZero);

    f.setLayout(null);
    f.setVisible(true);
  }

  // Observable simulating clock ticking every second
  final Observable<Integer> timer = Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
      new Thread() {
        @Override
        public void run() {
          try {
            while (true) {
              TimeUnit.MILLISECONDS.sleep(100);
              e.onNext(1);
            }
          } catch (java.lang.InterruptedException e) {
            System.out.println(e.toString());
          }
        }
      }.start();
    }
  });

  // Observer updating the display
  final Observer<Integer> display = new Observer<Integer>() {
    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(Integer value) {
      myUI.updateTime();
    }

    @Override
    public void onError(Throwable e) {
      System.out.println("onError: ");
    }

    @Override
    public void onComplete() {
      System.out.println("onComplete: All Done!");
    }
  };

  final Observer<Integer> displaySetRunningTrue = new Observer<Integer>() {
    public void onSubscribe(Disposable d) {
    }

    public void onNext(Integer value) {
      myUI.startTime();
    }

    public void onError(Throwable e) {
      System.out.println("onError: ");
    }

    public void onComplete() {
      System.out.println("onComplete: All Done!");
    }
  };

  final Observer<Integer> displaySetRunningFalse = new Observer<Integer>() {
    public void onSubscribe(Disposable d) {
    }

    public void onNext(Integer value) {
      myUI.stopTime();
    }

    public void onError(Throwable e) {
      System.out.println("onError: ");
    }

    public void onComplete() {
      System.out.println("onComplete: All Done!");
    }
  };

  final Observer<Integer> displaySetAllZero = new Observer<Integer>() {
    public void onSubscribe(Disposable d) {
    }

    public void onNext(Integer value) {
      myUI.resetTime();
    }

    public void onError(Throwable e) {
      System.out.println("onError: ");
    }

    public void onComplete() {
      System.out.println("onComplete: All Done!");
    }
  };
  Observable<Integer> rxPushStart = Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
      myUI.startButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ee) {
          e.onNext(1);
        }
      });
    }
  });

  Observable<Integer> rxPushStop = Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
      myUI.stopButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ee) {
          e.onNext(1);
        }
      });
    }
  });

  Observable<Integer> rxPushReset = Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
      myUI.resetButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ee) {
          e.onNext(1);
        }
      });
    }
  });

}


