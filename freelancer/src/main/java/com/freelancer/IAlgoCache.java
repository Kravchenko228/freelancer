package com.freelancer;

public interface IAlgoCache<K, V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
}
