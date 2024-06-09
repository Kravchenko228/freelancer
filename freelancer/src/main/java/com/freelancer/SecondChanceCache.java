package com.freelancer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class SecondChanceCache<K, V> implements IAlgoCache<K, V> {
    private final int capacity;
    private final Map<K, V> cache;
    private final Map<K, Boolean> referenceBits;
    private final Queue<K> queue;

    public SecondChanceCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.referenceBits = new HashMap<>();
        this.queue = new LinkedList<>();
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            referenceBits.put(key, true);
            return cache.get(key);
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        while (cache.size() >= capacity) {
            K currentKey = queue.poll();
            if (referenceBits.get(currentKey)) {
                referenceBits.put(currentKey, false);
                queue.offer(currentKey);
            } else {
                cache.remove(currentKey);
                referenceBits.remove(currentKey);
            }
        }
        cache.put(key, value);
        referenceBits.put(key, false);
        queue.offer(key);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        referenceBits.remove(key);
        queue.remove(key);
    }
}
