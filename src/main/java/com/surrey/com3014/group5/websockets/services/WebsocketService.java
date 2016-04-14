package com.surrey.com3014.group5.websockets.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * @author Aung Thu Moe
 */

public abstract class WebsocketService<K, V> {
    protected LoadingCache<K, V> cache = CacheBuilder.newBuilder().build(new CacheLoader<K, V>() {
        @Override
        public V load(K key) throws Exception {
            return init();
        }
    });

    abstract protected V init();
}
