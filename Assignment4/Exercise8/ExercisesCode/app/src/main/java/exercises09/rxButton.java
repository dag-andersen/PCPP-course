package exercises09;

import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

class rxButton {
  public static void main(String[] args){ new rxButton(); }

  private JFrame lf;
	private final JButton nameButton= new JButton("Name");
  
  public rxButton() { 
    JFrame f= new JFrame("Stopwatch");  	
		f.setBounds(0, 0, 220, 220);
    nameButton.setBounds(50, 50, 95, 25); 
  	f.add(nameButton); 
    f.setLayout(null);  
		f.setVisible(true);
 
    rxPush.subscribe(display);
  }	

  final Observable<Integer> rxPush= Observable.create(new ObservableOnSubscribe<Integer>() {
    @Override
    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
      nameButton.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent ee){  
          e.onNext(1);  
        }  
      }); 
    }
  }); 

  final Observer<Integer> display= new Observer<Integer>() {
    @Override
    public void onSubscribe(Disposable d) {  }
    @Override
    public void onNext(Integer value) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (java.lang.InterruptedException e) { System.out.println(e.toString()); };
      System.out.println("Pushed");
    }
    @Override
    public void onError(Throwable e) {System.out.println("onError: "); }
    @Override
    public void onComplete() { System.out.println("onComplete: All Done!");   }
  };
}
