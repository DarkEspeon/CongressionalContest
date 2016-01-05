package net.awesome.game.collections;

public interface IMap<K, V> {
	public boolean containsKey(K key);
	public Object[] keySet();
	public Object[] valueSet();
	public void add(K key, V value);
	public void add(K[] key, V[] value);
	public V get(K key);
	public V remove(K key);
	public void clear();
}
