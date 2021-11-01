package exercises09;

import java.awt.event.*;
import javax.swing.*;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
/* This example is inspired by the StopWatch app in Head First. Android Development
   http://shop.oreilly.com/product/0636920029045.do
   Modified to Java, October 7, 2021 by Jørgen Staunstrup, ITU, jst@itu.dk */

public class StopwatchN {
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        new StopwatchN(N);
    }

    public StopwatchN(int N) {
        final JFrame f = new JFrame("StopwatchN");
        

        for (int i = 0; i < N; i++) {
            final stopwatchUI myUI = new stopwatchUI(i * 220, f);

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

        }

        f.setBounds(0, 0, 220* N, 220);
        f.setLayout(null);
        f.setVisible(true);
    }
}
