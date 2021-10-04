package lectureCode;

//Used in Quicksort implemented with Executors
//Problem contains data structure for a single Problem
//Basic quicksort based on code in Peter Sestofts Benchmark node
// Version 09/09 - 2021

class Problem {
	public int[] arr;
	public int low, high;
	public Problem(int[] arr, int low, int high){
		this.arr= arr; this.low= low; this.high= high;
	}

  public int size(){return (high-low); }
}