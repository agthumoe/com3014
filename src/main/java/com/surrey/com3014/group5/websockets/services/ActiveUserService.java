package com.surrey.com3014.group5.websockets.services;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.websockets.domains.AccessTime;
import com.surrey.com3014.group5.websockets.dto.ActiveUserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aung Thu Moe
 */
@Service
public class ActiveUserService extends WebsocketService<ActiveUserDTO, AccessTime> {
    private static final int SCHEDULE_INTERVAL = 5000;

    @Override
    protected AccessTime init() {
        return new AccessTime();
    }

    public void update(ActiveUserDTO user) {
        this.cache.getUnchecked(user).update();
    }

    public List<ActiveUserDTO> getActiveUsers() {
        final List<ActiveUserDTO> activeUsers = new ArrayList<>();
        this.cache.asMap().keySet().stream().filter(
            user -> (System.currentTimeMillis() - this.cache.getUnchecked(user).getLastAccess()) < SCHEDULE_INTERVAL
        ).forEach(activeUsers::add);
        return activeUsers;
    }
}
