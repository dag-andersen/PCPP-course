package exercises03;

public class PerfTest {
    private volatile int vCtr;
    private int ctr;
    
    public void vInc () {
        vCtr++;
    }
    public void inc () {
        ctr++;
    }
}
