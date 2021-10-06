import java.util.*;
class ProblemHeap {
	private List<Problem> heap= new LinkedList<Problem>();

	private int part; // part > 0 indicates that the heap is not empty or some threads are working on problems
                    // part <= 0 no more problems to solve

	public ProblemHeap(int part) {
		this.part= part;
	}

	public void add(Problem newProblem) {
		synchronized(heap) {
			heap.add(newProblem);
			heap.notify();  // will wake up a single thread 
		}
	}

	public Problem getProblem() throws InterruptedException {
		// Invariant if problem returned is not null part is unchanged
		// 				   if null returned part <= 0
		synchronized(heap) {
			while ( heap.size() == 0 ) {
				part= part-1;
				if (part <= 0) return null;				
				heap.wait();
				part= part+1; 
			}
			return heap.remove(0);
		}
	}
}