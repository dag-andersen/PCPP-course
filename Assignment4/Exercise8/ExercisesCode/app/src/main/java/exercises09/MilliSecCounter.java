package exercises09;

public class MilliSecCounter {
  public int milliSeconds;
  private boolean running;

  public MilliSecCounter(int s, boolean r){
    milliSeconds= s;
    running= r;
  }
	
  public void setRunning(boolean running) {
    this.running= running;
  }

  public boolean incr(){
    if (running) milliSeconds+=100; 
    return running;
  }

  public boolean running(){
    return this.running;
  }
}
