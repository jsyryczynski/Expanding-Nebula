import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

class Pair {
	Pair(short ce, short pe) {
		currentElement = ce;
		previousElement = pe;
	}
	short currentElement;
	short previousElement;
}

public class Solution {
	public static void main(String [] input) {
		System.out.println(solution(new boolean[][]{{true, false, true}, {false, true, false}, {true, false, true}}));
		System.out.println(solution(new boolean[][]{
			{true, false, true, false, false, true, true, true},
			{true, false, true, false, false, false, true, false},
			{true, true, true, false, false, false, true, false},
			{true, false, true, false, false, false, true, false},
			{true, false, true, false, false, true, true, true}
			}));
	}
	
	public static short calcPostColumn(short current, short previous, int size) {
		short result = 0;
		for (int idx = 0; idx < size; ++idx) {
			int bit1 = (current & (1<<idx)) >> idx;
			int bit2 = (current & (1<< (idx+1))) >> (idx+1);
			int bit3 = (previous & (1<<idx)) >> idx;
			int bit4 = (previous & (1<< (idx+1))) >> (idx+1);
			int sum = bit1 + bit2 + bit3 + bit4;
			if (sum == 1) {
				result |= 1 << idx;	
			}
		}
		return result;
	} 
	
	public static int solution(boolean[][] g) {
		int height = g.length;
		int width = g[0].length;
		
		int possibilitiesCount = 2 << height;
		
		int[] countArray = new int[possibilitiesCount];
		
		ArrayList<Short> inputArray = new ArrayList<>();
		for(int xIdx = 0; xIdx < width; ++xIdx) {
			short value = 0;
			for (int yIdx = 0; yIdx < height; ++yIdx) {
				if (g[yIdx][xIdx])
					value |= 1 << yIdx;
			}
			inputArray.add(value);
		}
		
		// Prepare countArray
		for (int idx = 0; idx < possibilitiesCount; ++idx) {
			countArray[idx] = 1;
		}
		
		// Prepare pre-array
		HashMap<Short, LinkedList<Pair>> elementToPrePair = new HashMap<>();
		for(short idx1 = 0; idx1 < possibilitiesCount; ++idx1) {
			elementToPrePair.put(idx1, new LinkedList<>());
		}
		
		for(short idx1 = 0; idx1 < possibilitiesCount; ++idx1) {
			for(short idx2 = 0; idx2 < possibilitiesCount; ++idx2) {
				short l = calcPostColumn(idx1, idx2, height);
				elementToPrePair.get(l).add(new Pair(idx1 , idx2));
			}
		}
		
		// go through calculations
		for (int wIdx = 0; wIdx < width; ++wIdx) {
			
			int[] newCount = new int[possibilitiesCount];
			for (Pair preElement : elementToPrePair.get(inputArray.get(wIdx)) ) {
				newCount[preElement.currentElement] += countArray[preElement.previousElement];
			}  
			countArray = newCount;			
		}
		
		// Sum result
		int sum = 0;
		for (int idx = 0; idx < possibilitiesCount; ++idx) {
			sum += countArray[idx];
		}
		
		return sum;
	}
}
