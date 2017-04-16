package com.learn.soft.product.util.cache;

import java.util.ArrayList;
import java.util.List;

public class L2Cache<K, V> implements ICache<K, V> {
	private List<ICache<K, V>> caches=new ArrayList<ICache<K,V>>(2);
	private ICacheStrategy<K, V> cacheStrategy;
	public void setCache(ICache<K, V> cache, int level){
		if(level<1||level>2){
			throw new IllegalArgumentException("level must be either 1 or 2");
		}
		caches.add(level-1, cache);
	}
	
	public void setCacheStrategy(ICacheStrategy<K, V> cs){
		cacheStrategy=cs;
	}
	
	private void checkStragegy() {
		if (null==cacheStrategy) {
			throw new IllegalStateException("no cache stragery");
		}
	}
	
	@Override
	public V put(K key, V value) {
		checkStragegy();
		return cacheStrategy.doPut(caches, key, value);
	}

	@Override
	public V get(K key) {
		checkStragegy();
		return cacheStrategy.doGet(caches, key);
	}

	@Override
	public V remove(K key) {
		checkStragegy();
		return cacheStrategy.doRemove(caches, key);
	}

	@Override
	public void clear() {
		checkStragegy();
		cacheStrategy.doClear(caches);
	}
	
	public void clear(int level){
		checkStragegy();
		caches.get(level-1).clear();
	}

}
