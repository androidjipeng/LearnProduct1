package com.learn.soft.product.util.cache;

import java.util.List;

public interface ICacheStrategy<K, V> {
	V doPut(List<ICache<K, V>> caches, K key, V value);
	
	V doGet(List<ICache<K, V>> caches, K key);
	
	V doRemove(List<ICache<K, V>> caches, K key);
	
	void doClear(List<ICache<K, V>> caches);
}
