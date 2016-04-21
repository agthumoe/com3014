package com.surrey.com3014.group5.websockets.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Abstract websocket service which has a cache to store centain information.
 *
 * @author Aung Thu Moe
 */

public abstract class WebsocketService<K, V> {
    /**
     * Cache of this websocket service.
     */
    protected LoadingCache<K, V> cache = CacheBuilder.newBuilder().build(new CacheLoader<K, V>() {
        @Override
        public V load(K key) throws Exception {
            return init();
        }
    });

    /**
     * Init method should return a new V object created.
     *
     * @return new V object.
     */
    abstract protected V init();
}
