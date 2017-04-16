package com.learn.soft.product.util.cache;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2014/11/26
 * <p/>
 * 描述：
 * **********************************************************
 */
public abstract class BaseModelProxy<Params, Model> {
    private Model mModel;
    private String mLastKey;
    private ICache<String, Model> mCache;
    private Object sync = new Object();

    public BaseModelProxy(String cacheName) {
        if (cacheName != null) {
            mCache = new CacheHelp<String, Model>().createCache(cacheName);
        }
    }

    private void putCache(String key, Model value) {
        if (mCache == null) {
            return;
        }
        if (value == null) {
            mCache.remove(key);
        } else {
            mCache.put(key, value);
        }
    }

    private Model getCache(String key) {
        if (mCache == null) {
            return null;
        }
        return mCache.get(key);
    }

    public void cleanByKey(String key) {
        if (mCache != null) {
            mCache.remove(key);
        }
    }

    public void cleanAll() {
        if (mCache != null) {
            mCache.clear();
        }
    }

    public Model getmModel() {
        return mModel;
    }

    public void cleanModel(){
        mModel = null;
    }

    /**
     * 只查询本地缓存数据，如果没有命中，则返回null
     *
     * @param args
     * @return
     */
    public final Model hitLocal(Params... args) {
        synchronized (sync) {
            String key = getKey(args);
            compareKey(key);
            if (mModel == null) {
                mModel = getCache(key);
            }
        }
        return mModel;
    }

    public final void putLocal(Model model, Params... args) {
        String key = getKey(args);
        putCache(key, model);
    }


    private void compareKey(String key) {
        if (!key.equals(mLastKey)) {
            mModel = null;
            mLastKey = key;
        }
    }

    protected abstract String getKey(Params... args);
}
