package com.freelancer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomCache<K, V> implements IAlgoCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;
    private final Random random;

    public RandomCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.random = new Random();
    }

    @Override
    public V get(K key) {
        return cache.getOrDefault(key, null);
    }

    @Override
    public void put(K key, V value) {
        if (cache.size() >= capacity) {
            K randomKey = cache.keySet().toArray((K[]) new Object[0])[random.nextInt(cache.size())];
            cache.remove(randomKey);
        }
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }
}
