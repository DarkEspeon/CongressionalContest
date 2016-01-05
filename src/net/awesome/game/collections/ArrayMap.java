package net.awesome.game.collections;


public class ArrayMap<K, V> implements IMap<K, V>{
	private Object[] keys;
	private Object[] values;
	
	private int size = 0, capacity;
	
	public ArrayMap(){
		capacity = 10;
		keys = new Object[capacity];
		values = new Object[capacity];
	}
	public ArrayMap(int capacity){
		this.capacity = capacity;
		keys = new Object[capacity];
		values = new Object[capacity];
	}
	private void ensureSpace(int next){
		if(next < capacity) return;
		capacity *= 1.5;
		Object[] nkeys = new Object[capacity];
		Object[] nvalues = new Object[capacity];
		System.arraycopy(keys, 0, nkeys, 0, keys.length);
		System.arraycopy(values, 0, nvalues, 0, values.length);
		keys = nkeys;
		values = nvalues;
	}
	
	public int size(){ return size; }
	public Object[] keySet(){ return keys; }
	public Object[] valueSet(){ return values; }
	
	public boolean containsKey(K key){
		return getIndexOf(key) >= 0;
	}
	public void add(K key, V value) {
		if(containsKey(key)){
			int next = getIndexOf(key);
			keys[next] = key;
			values[next] = value;
		} else {
			int next = size;
			ensureSpace(next);
			keys[next] = key;
			values[next] = value;
			size++;
		}
	}
	public void add(K[] keys, V[] values) {
		for(int i = 0; i < keys.length && i < values.length; i++){
			add(keys[i], values[i]);
		}
	}
	private int getIndexOf(K keyCheck){
		int index = 0;
		for(Object k : keys){
			K key = (K)k;
			if(keyCheck.equals(key)) return index;
			index++;
		}
		return -1;
	}
	public V get(K key) {
		int index = getIndexOf(key);
		if(index >= 0) return (V)values[getIndexOf(key)];
		else return null;
	}
	public V remove(K key) {
		int index = getIndexOf(key);
		if(index >= 0) return null;
		V value = (V)values[index];
		System.arraycopy(keys, index + 1, keys, index, keys.length - index);
		System.arraycopy(values, index + 1, values, index, values.length - index);
		values[size - 1] = null;
		keys[size - 1] = null;
		size--;
		return value;
	}
	public void clear(){
		capacity = 10;
		size = 0;
		keys = new Object[capacity];
		values = new Object[capacity];
	}
}
