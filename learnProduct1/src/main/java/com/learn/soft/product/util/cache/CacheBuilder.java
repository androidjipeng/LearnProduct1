package com.learn.soft.product.util.cache;

public class CacheBuilder<K, V> {
	public static enum CacheType{
		MEM, DISK, DEFAULT_L2
	}
	
	private CacheType type;
	private int memMaxSize=-1;//-1表示没有限制
	private long diskExpiredPeriod=-1;//-1表示永久保存
	private String diskPath;
	private String cacheName;
	
	public CacheBuilder<K, V> setType(CacheType t){
		type=t;
		return this;
	}
	
	public CacheBuilder<K, V> setMemMaxSize(int size){
		memMaxSize=size;
		return this;
	}
	
	public CacheBuilder<K, V> setDiskPath(String path){
		diskPath=path;
		return this;
	}
	
	public CacheBuilder<K, V> setDiskExpiredPeriod(long timelong){
		diskExpiredPeriod=timelong;
		return this;
	}
	
	public CacheBuilder<K, V> setCacheName(String name){
		cacheName=name;
		return this;
	}
	
	public ICache<K, V> create(){
		if(type==null){
			throw new IllegalStateException();
		}
		switch (type) {
		case DEFAULT_L2:
			return createDefaultL2Cache();

		default:
			throw new UnsupportedOperationException();
		}
	}
	
	private L2Cache<K, V> createDefaultL2Cache(){
		if(null==diskPath||null==cacheName){
			throw new IllegalStateException();
		}
		L2Cache<K, V> cache=new L2Cache<K, V>();
		cache.setCache(new MemCache<K, V>(), 1);
		FileCache<K, V> fileCache=new FileCache<K, V>(diskPath, cacheName);
		fileCache.setmExpireTime(diskExpiredPeriod);
		cache.setCache(fileCache, 2);
		cache.setCacheStrategy(new DefaultL2CacheStrategy<K, V>());
		return cache;
	}
	
}
