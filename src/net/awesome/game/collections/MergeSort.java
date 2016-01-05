package net.awesome.game.collections;

public final class MergeSort {
	private static String[] strArray, tempStrArray;
	private static Object[] objArray, tempObjArray;
	private MergeSort(){}
	public static void sort(String[] strInArray, Object[] objInArray){
		strArray = new String[strInArray.length];
		objArray = new Object[objInArray.length];
		tempStrArray = new String[strInArray.length];
		tempObjArray = new Object[objInArray.length];
		System.arraycopy(strInArray, 0, strArray, 0, strInArray.length);
		System.arraycopy(objInArray, 0, objArray, 0, objInArray.length);
		doMergeSort(0, strInArray.length - 1);
	}
	private static void doMergeSort(int lower, int higher){
		if(lower < higher){
			int middle = lower + (higher - lower) / 2;
			doMergeSort(lower, middle);
			doMergeSort(middle + 1, higher);
			mergeParts(lower, middle, higher);
		}
	}
	private static void mergeParts(int lower, int middle, int higher){
		for(int i = lower; i <= higher; i++){
			tempStrArray[i] = strArray[i];
			tempObjArray[i] = objArray[i];
		}
		int i = lower;
		int j = middle + 1;
		int k = lower;
		while(i <= middle && j <= higher){
			if(tempStrArray[j] == null){
				j++;
				continue;
			} else if(tempStrArray[i] == null){
				i++;
				continue;
			}
			if(tempStrArray[i].compareToIgnoreCase(tempStrArray[j]) <= 0){
				strArray[k] = tempStrArray[i];
				objArray[k] = tempObjArray[i];
				i++;
			} else {
				strArray[k] = tempStrArray[j];
				objArray[k] = tempObjArray[j];
				j++;
			}
			k++;
		}
		while(i <= middle){
			strArray[k] = tempStrArray[i];
			objArray[k] = tempObjArray[i];
			k++;
			i++;
		}
	}
	public static String[] getSortedStrings(){
		return strArray;
	}
	public static Object[] getSortedObjects(){
		return objArray;
	}
}