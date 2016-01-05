package net.awesome.game.collections;

public class StringCache<V> implements IMap<String, V>{
	
	private String[] keys;
	private Object[] values;
	private int size, capacity;
	public StringCache(){
		capacity = 10;
		size = 0;
		keys = new String[capacity];
		values = new Object[capacity];
	}
	public boolean containsKey(String key) {
		return getIndex(key) >= 0;
	}
	private int getIndex(String key){
		return binarySearch(0, size - 1, 0, key);
	}
	private int binarySearch(int start, int end, int count, String key){
		if(start > end){
			return -1;
		}
		int mid = start + (end - start) / 2;
		//if(mid >= end) return -1;
		if(keys[mid] == null) return -1;
		
		if(keys[mid].equalsIgnoreCase(key)){
			return mid;
		} else if(keys[mid].compareToIgnoreCase(key) > 0){
			return binarySearch(start, mid - 1, count + 1, key);
		} else if(keys[mid].compareToIgnoreCase(key) < 0){
			return binarySearch(mid + 1, end, count + 1, key);
		} else return -1;
	}
	private void sort(){
		MergeSort.sort(keys, values);
		String[] nkeys = MergeSort.getSortedStrings();
		Object[] nvals = MergeSort.getSortedObjects();
		System.arraycopy(nkeys, 0, keys, 0, nkeys.length);
		System.arraycopy(nvals, 0, values, 0, nvals.length);
	}
	public String[] keySet() {
		return keys;
	}

	public Object[] valueSet() {
		return values;
	}
	private void ensureSpace(int next){
		if(next < capacity) return;
		capacity *= 1.5;
		String[] nkeys = new String[capacity];
		Object[] nvalues = new Object[capacity];
		System.arraycopy(keys, 0, nkeys, 0, keys.length);
		System.arraycopy(values, 0, nvalues, 0, values.length);
		keys = nkeys;
		values = nvalues;
	}
	public void add(String key, V value) {
		int index = getIndex(key);
		if(index >= 0){
			keys[index] = key;
			values[index] = value;
		} else {
			index = size;
			ensureSpace(index);
			keys[index] = key;
			values[index] = value;
			size++;
		}
		sort();
	}

	public void add(String[] key, V[] value) {
		for(int i = 0; i < key.length && i < value.length; i++){
			add(key[i], value[i]);
		}
	}

	public V get(String key) {
		int index = getIndex(key);
		return (index >= 0) ? (V)values[index] : null;
	}

	public V remove(String key) {
		int index = getIndex(key);
		if(index < 0) return null;
		V value = (V)values[index];
		System.arraycopy(keys, index + 1, keys, index, keys.length - index);
		System.arraycopy(values, index + 1, values, index, values.length - index);
		values[size - 1] = null;
		keys[size - 1] = null;
		size--;
		sort();
		return value;
	}

	public void clear() {
		capacity = 10;
		size = 0;
		keys = new String[capacity];
		values = new Object[capacity];
	}
	
}
