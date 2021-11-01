package exercises09;

import java.awt.event.*;  
import javax.swing.*; 
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
// User interfaca for Stopwatch, October 7, 2021 by Jørgen Staunstrup, ITU, jst@itu.dk

class stopwatchUI {
  final private String allzero = "0:00:00:000";
  private int lx;
  private static JFrame lf;
  private MilliSecCounter lC;
  
  final private JButton startButton= new JButton("Start");	
  final private JButton stopButton= new JButton("Stop");
  final private JButton resetButton= new JButton("Reset");		
  final private JTextField tf= new JTextField();
  
  public stopwatchUI(int x, JFrame jF){
    lx= x+50; lf= jF;	
    tf.setBounds(lx, 10, 120, 20); 
    tf.setText(allzero);

    lC= new MilliSecCounter(0, false);

    startButton.setBounds(lx, 50, 95, 25); 
    startButton.addActionListener(new ActionListener(){  
      public void actionPerformed(ActionEvent e){  
              lC.setRunning(true);  
          }  
      });  
      
    stopButton.setBounds(lx, 90, 95, 25); 
    stopButton.addActionListener(new ActionListener(){  
      public void actionPerformed(ActionEvent e){  
              lC.setRunning(false); 
          }  
      });

    resetButton.setBounds(lx, 130, 95, 25); 				
    resetButton.addActionListener(new ActionListener(){  
      public void actionPerformed(ActionEvent e){  
        synchronized(this) {
          lC= new MilliSecCounter(0, false);
          tf.setText(allzero);
        }} 
      }); 

    // set up user interface
    lf.add(tf);
    lf.add(startButton);  
    lf.add(stopButton);
    lf.add(resetButton);
  }

  public boolean running() { return lC.running();  }
  
  public void updateTime(){
    synchronized(this) {
      if ( lC.incr() ) {
        int milliseconds = lC.milliSeconds;
        int secs = milliseconds/1000;

        int seconds= secs%60;
        int hours= secs/3600;
        int minutes= (secs%3600)/60;

        int milli = milliseconds%1000;
        String time= String.format(Locale.getDefault(),	"%d:%02d:%02d:%03d", hours, minutes, seconds, milli);
        tf.setText(time);
      }
    }
  };
}