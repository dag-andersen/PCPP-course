class Problem {
  // Invariant represent problem ranging from low to (and including) high
	public int[] arr;
	public int low, high;
	public Problem(int[] arr, int low, int high){
		this.arr= arr; this.low= low; this.high= high;
	}
}