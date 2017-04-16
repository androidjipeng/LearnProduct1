package com.learn.soft.product.util.cache;

public interface ICache<K, V> {
	V put(K key, V value);
	V get(K key);
	V remove(K key);
	void clear();
}
