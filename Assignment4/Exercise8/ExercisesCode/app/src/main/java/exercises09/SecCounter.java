package exercises09;

public class SecCounter {
  public int seconds;
  private boolean running;

  public SecCounter(int s, boolean r){
    seconds= s;
    running= r;
  }
	
  public void setRunning(boolean running) {
    this.running= running;
  }

  public boolean incr(){
    if (running) seconds++; 
    return running;
  }

  public boolean running(){
    return this.running;
  }
}
