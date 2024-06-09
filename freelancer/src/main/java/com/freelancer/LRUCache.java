package com.freelancer;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> implements IAlgoCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.capacity;
            }
        };
    }

    @Override
    public void put(K key, V value) {
        System.out.println("putting key: " + key + " value: " + value);
        cache.put(key, value);
    }

    @Override
    public V get(K key) {
        System.out.println("getting key: " + key);
        return cache.get(key);
    }

    @Override
    public void remove(K key) {
        System.out.println("removing key: " + key);
        cache.remove(key);
    }
    
}
