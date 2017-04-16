package com.learn.soft.product.util.cache;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class FileCache<K, V> implements ICache<K, V> {
	private String mIdentifier;
	private String mPath;
	private File mDir;
	private long mExpireTime=-1;//-1表示永久保存,如设置3天为3*24*60*60*1000
	private long mMaxSpace=-1;//-1表示没有限制，如10M=10*1024*1024
	
	public FileCache(String path, String i) {
		mIdentifier=i;
		mPath=path+mIdentifier;
		mDir=new File(mPath);
	}

	public void setmExpireTime(long mExpireTime) {
		this.mExpireTime = mExpireTime;
	}

	public void setmMaxSpace(long mMaxSpace) {
		this.mMaxSpace = mMaxSpace;
	}
	
	private void prepareCatalogDir() {
		if (!mDir.exists()) {
			mDir.mkdir();
		}
	}

	@Override
	public V put(K key, V value) {
		prepareCatalogDir();
		if (isFileCacheOutOfSpace()) {
			clear();
		}
		File file=new File(mPath, String.valueOf(key));
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fileOutputStream=new FileOutputStream(file);
			BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(fileOutputStream);
			ObjectOutputStream objectOutputStream=new ObjectOutputStream(bufferedOutputStream);
			try {
				objectOutputStream.writeObject(value);
			} finally {
				objectOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {
		prepareCatalogDir();
		if (isFileCacheExpired(String.valueOf(key))) {
			remove(key);
			return null;
		}
		File file=new File(mPath, String.valueOf(key));
		if (!file.exists()) {
			return null;
		}
		try {
			FileInputStream fileInputStream=new FileInputStream(file);
			BufferedInputStream bufferedInputStream=new BufferedInputStream(fileInputStream);
			ObjectInputStream objectInputStream=new ObjectInputStream(bufferedInputStream);
			try {
				return (V) objectInputStream.readObject();
			} finally {
				objectInputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (file.exists()) {
				file.delete();
			}
		}
		return null;
	}

	@Override
	public V remove(K key) {
		File file = new File(mPath, String.valueOf(key));
		if(file.exists()){
			file.delete();
		}
		return null;
	}

	@Override
	public void clear() {
		try {
			FileUtils.deleteDirectory(new File(mPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isFileCacheOutOfSpace(){
		return mMaxSpace==-1? false: FileUtils.sizeOfDirectory(mDir)>mMaxSpace;
	}
	
	private boolean isFileCacheExpired(String fileName){
		if(mExpireTime==-1){
			return false;
		}
		File file=new File(mPath, fileName);
		if (!file.exists()) {
			return false;
		}
		return (System.currentTimeMillis()-file.lastModified())>mExpireTime;
	}

}
