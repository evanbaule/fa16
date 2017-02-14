package txt;

public class Recursion {
	public static int what(int i, int[] arr){
		int retVal = 0;
		if(i >= 0 && i < arr.length && arr != null && arr.length > 0){
			if(arr[i] == 2*i){
				retVal = 1 + what(i+1, arr);
			}
			else {
				retVal = what(i+1, arr) - 1;
			}
		}
		return retVal;
	}
	public static void main(String[] args){
		System.out.println(what(0, new int[] {1, 2, 3, 4, 5}));
		System.out.println(what(0, new int[] {1, 2, 4, 7, 8}));
	}
}
