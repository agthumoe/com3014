package com.surrey.com3014.group5.services.activeuser;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Aung Thu Moe
 */
@Service
public class ActiveUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveUserService.class);
    private LoadingCache<User, UserStats> statsByUser = CacheBuilder.newBuilder().build(new CacheLoader<User, UserStats>() {

        @Override
        public UserStats load(User key) throws Exception {
            return new UserStats();
        }

    });

    public void mark(User user) {
        statsByUser.getUnchecked(user).mark();
    }

    public List<UserDTO> getActiveUsers() {
        List<UserDTO> active = new ArrayList<>();
        // has the user checked in within the last 10 seconds?
        statsByUser.asMap().keySet().stream().filter(user -> (System.currentTimeMillis() - statsByUser.getUnchecked(user).lastAccess()) < 5000).forEach(user -> {
            UserDTO userDTO = new UserDTO(user);
            active.add(userDTO);
        });
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
