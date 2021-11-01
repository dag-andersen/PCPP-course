package exercises09;

import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class TextAndButton {
	private static JFrame lf;
	private static JButton nameButton= new JButton("Stop");
  private static JTextField userText= new JTextField(14);
  public static void main(String[] args){ new TextAndButton(); }
  
  public TextAndButton() { 
    JFrame f=new JFrame("Stopwatch");  	
		f.setBounds(0, 0, 220, 220);
    nameButton.setBounds(20, 20, 70, 25); 
    userText.setBounds(20, 100, 120, 25);
    f.add(userText);
  	f.add(nameButton); 
    f.setLayout(null);  
		f.setVisible(true);
 
    rxPush.subscribe(display);
  }	

  final static Observable<Integer> rxPush
    = Observable.create(new ObservableOnSubscribe<Integer>() {
      @Override
      public void subscribe(ObservableEmitter<Integer> e) throws Exception {
        nameButton.addActionListener(new ActionListener(){  
          public void actionPerformed(ActionEvent ee){  
            e.onNext(1);  
          }  
        }); 
      }
    }); 

  final static Observer<Integer> display= new Observer<Integer>() {
    @Override
    public void onSubscribe(Disposable d) {  }
    @Override
    public void onNext(Integer value) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (java.lang.InterruptedException e) {
                System.out.println(e.toString());
      };
      System.out.println("Pushed");
    }
    @Override
    public void onError(Throwable e) {System.out.println("onError: "); }
    @Override
    public void onComplete() { System.out.println("onComplete: All Done!");   }
  };
}
