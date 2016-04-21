package com.surrey.com3014.group5.websockets.services;

import com.surrey.com3014.group5.websockets.domains.AccessTime;
import com.surrey.com3014.group5.websockets.domains.EloRating;
import com.surrey.com3014.group5.websockets.dto.ActiveUserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This service stores the user access time in the cache.
 *
 * @author Aung Thu Moe
 */
@Service
public class ActiveUserService extends WebsocketService<ActiveUserDTO, AccessTime> {
    /**
     * Default schedule interval as 5 sec.
     */
    private static final int SCHEDULE_INTERVAL = 5000;

    /**
     * Initialise new AccessTime object for cache.
     *
     * @return new AccessTime object.
     */
    @Override
    protected AccessTime init() {
        return new AccessTime();
    }

    /**
     * Update the accessTime of the provided user.
     *
     * @param user provided activeUser.
     */
    public void update(ActiveUserDTO user) {
        this.cache.getUnchecked(user).update();
    }

    /**
     * Update elo rating of the provided user.
     *
     * @param rating elo rating.
     */
    public void updateUserRating(EloRating rating) {
        for (Map.Entry<ActiveUserDTO, AccessTime> entry : this.cache.asMap().entrySet()) {
            ActiveUserDTO userDTO = entry.getKey();
            if (userDTO.getId() == rating.getUserId()) {
                userDTO.setRating(rating.getRating());
            }
        }
    }

    /**
     * Get all active users from the cache who are active within last 5 sec.
     *
     * @return all active users who are active within last 5 sec.
     */
    public List<ActiveUserDTO> getActiveUsers() {
        final List<ActiveUserDTO> activeUsers = new ArrayList<>();
        this.cache.asMap().keySet().stream().filter(
            user -> (System.currentTimeMillis() - this.cache.getUnchecked(user).getLastAccess()) < SCHEDULE_INTERVAL
        ).forEach(activeUsers::add);
        return activeUsers;
    }
}
