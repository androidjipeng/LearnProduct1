package com.learn.soft.product.util.cache;

import java.util.List;

public class DefaultL2CacheStrategy<K, V> implements ICacheStrategy<K, V> {
	private static final int TAG_FIRST_CACHE=0;
	private static final int TAG_SECOND_CACHE=1;
	@Override
	public V doPut(List<ICache<K, V>> caches, K key, V value) {
		V mValue=null;
		if(null!=value){
			mValue=caches.get(TAG_SECOND_CACHE).put(key, value);
		}
		return mValue;
	}

	@Override
	public V doGet(List<ICache<K, V>> caches, K key) {
		V value=caches.get(TAG_FIRST_CACHE).get(key);
		if(null!=value){
			return value;
		}else{
			value=caches.get(TAG_SECOND_CACHE).get(key);
		}
		
		if(null!=value){
			caches.get(TAG_FIRST_CACHE).put(key, value);
			return value;
		}else {			
			return null;
		}
	}

	@Override
	public V doRemove(List<ICache<K, V>> caches, K key) {
		V mValue=null;
		for (ICache<K, V> iCache : caches) {
			V v=iCache.remove(key);
			if (null==mValue) {
				mValue=v;
			}
		}
		return mValue;
	}

	@Override
	public void doClear(List<ICache<K, V>> caches) {
		for (ICache<K, V> iCache : caches) {
			iCache.clear();
		}
	}

}
