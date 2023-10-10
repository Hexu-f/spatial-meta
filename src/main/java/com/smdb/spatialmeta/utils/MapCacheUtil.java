package com.smdb.spatialmeta.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这是一个内存缓存工具
 */
public class MapCacheUtil {

    private static MapCacheUtil mapCache;
    private Map<Object, Object> cacheItems;

    private MapCacheUtil() {
        cacheItems = new ConcurrentHashMap<Object, Object>();
    }

    /**
     * 获取唯一实例
     *
     * @return instance
     */
    public static MapCacheUtil getInstance() {
        if (mapCache == null) {
            synchronized (MapCacheUtil.class) {
                if (mapCache == null) {
                    mapCache = new MapCacheUtil();
                }
            }
        }
        return mapCache;
    }

    /**
     * 获取所有cache信息
     *
     * @return cacheItems
     */
    public Map<Object, Object> getCacheItems() {
        return this.cacheItems;
    }

    /**
     * 清空cache
     */
    public void clearAllItems() {
        cacheItems.clear();
    }

    /**
     * 获取指定cache信息
     *
     * @param key 唯一标识
     * @return Object cacheItem
     */
    public Object getCacheItem(Object key) {
        if (cacheItems.containsKey(key)) {
            return cacheItems.get(key);
        }
        return null;
    }

    /**
     * 存值
     *
     * @param key   唯一标识
     * @param value 存放的值
     */
    public Boolean putCacheItem(Object key, Object value) {
        cacheItems.put(key, value);
        return true;
    }

    /**
     * 根据key删除
     *
     * @param key 唯一标识
     */
    public void removeCacheItem(Object key) {
        if (cacheItems.containsKey(key)) {
            cacheItems.remove(key);
        }
    }

    /**
     * 获取cache长度
     *
     * @return size
     */
    public int getSize() {
        return cacheItems.size();
    }

}
