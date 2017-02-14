package project;

public class Memory {
	// Initialize Data size
	private int changedIndex = -1;
	public static final int DATA_SIZE = 2048;
	private int[] data = new int[DATA_SIZE];

	public int getChangedIndex(){
		return changedIndex;
	}
	
	public int[] getData() {
		return data;
	}

	public int getData(int idx) {
		return data[idx];
	}

	public void clear(int start, int end){
		for(int i = start; i < end; i++){
			data[i] = 0;
		}
		changedIndex = -1;
	}
	
	public void setData(int idx, int val) {
		data[idx] = val;
	}

}
