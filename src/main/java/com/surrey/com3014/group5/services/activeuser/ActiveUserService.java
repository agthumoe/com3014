package com.surrey.com3014.group5.services.activeuser;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Aung Thu Moe
 */
@Component
public class ActiveUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveUserService.class);
    private LoadingCache<String, UserStats> statsByUser = CacheBuilder.newBuilder().build(new CacheLoader<String, UserStats>() {

        @Override
        public UserStats load(String key) throws Exception {
            return new UserStats();
        }

    });

    public void mark(String username) {
        LOGGER.debug("User: {}, is still active " ,username);
        statsByUser.getUnchecked(username).mark();
    }

    public Set<String> getActiveUsers() {
        Set<String> active = Sets.newTreeSet();
        // has the user checked in within the last 15 seconds?
        active.addAll(statsByUser.asMap().keySet().stream().filter(user -> (System.currentTimeMillis() - statsByUser.getUnchecked(user).lastAccess()) < 15000).collect(Collectors.toList()));
        return active;
    }

    private static class UserStats {

        private AtomicLong lastAccess = new AtomicLong(System.currentTimeMillis());

        private void mark() {
            lastAccess.set(System.currentTimeMillis());
        }

        private long lastAccess() {
            return lastAccess.get();
        }

    }
}
