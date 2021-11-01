package exercises09;

import java.awt.event.*;
import javax.swing.*;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
/* This example is inspired by the StopWatch app in Head First. Android Development
   http://shop.oreilly.com/product/0636920029045.do
   Modified to Java, October 7, 2021 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class Stopwatch2 {
  public static void main(String[] args) {
    new Stopwatch2();
  }

  public Stopwatch2() {
    final JFrame f = new JFrame("Stopwatch1");
    final JFrame f2 = new JFrame("Stopwatch2");
    final stopwatchUI myUI = new stopwatchUI(0, f);
    final stopwatchUI myUI2 = new stopwatchUI(0, f2);
    f.setBounds(0, 0, 220, 220);
    f.setLayout(null);
    f.setVisible(true);

    f2.setBounds(0, 0, 220, 220);
    f2.setLayout(null);
    f2.setVisible(true);

    // Background Thread simulating a clock ticking every 100 milliseconds
    new Thread() {
      @Override
      public void run() {
        try {
          while (true) {
            TimeUnit.MILLISECONDS.sleep(100);
            myUI.updateTime();
          }
        } catch (java.lang.InterruptedException e) {
          System.out.println(e.toString());
        }
      }
    }.start();

    // Background Thread simulating a clock ticking every 100 milliseconds
    new Thread() {
      @Override
      public void run() {
        try {
          while (true) {
            TimeUnit.MILLISECONDS.sleep(100);
            myUI2.updateTime();
          }
        } catch (java.lang.InterruptedException e) {
          System.out.println(e.toString());
        }
      }
    }.start();
  }
}
