package com.learn.soft.product.util.cache;

import android.support.v4.util.LruCache;

public class MemCache<K, V> extends LruCache<K, V> implements ICache<K, V> {
	private static final int DEFAULT_MEM_SIZE=1 * 1024 * 1024; // 1MiB
    public MemCache() {
		super(DEFAULT_MEM_SIZE);
	}

	@Override
	public void clear() {
		super.evictAll();
	}
}
