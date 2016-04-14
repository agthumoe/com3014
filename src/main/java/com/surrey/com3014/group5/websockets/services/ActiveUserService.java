package com.surrey.com3014.group5.websockets.services;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.websockets.domains.AccessTime;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aung Thu Moe
 */
@Service
public class ActiveUserService extends WebsocketService<UserDTO, AccessTime> {
    private static final int SCHEDULE_INTERVAL = 5000;

    @Override
    protected AccessTime init() {
        return new AccessTime();
    }

    public void update(UserDTO user) {
        this.cache.getUnchecked(user).update();
    }

    public List<UserDTO> getActiveUsers() {
        final List<UserDTO> activeUsers = new ArrayList<>();
        this.cache.asMap().keySet().stream().filter(
            user -> (System.currentTimeMillis() - this.cache.getUnchecked(user).getLastAccess()) < SCHEDULE_INTERVAL
        ).forEach(activeUsers::add);
        return activeUsers;
    }
}
