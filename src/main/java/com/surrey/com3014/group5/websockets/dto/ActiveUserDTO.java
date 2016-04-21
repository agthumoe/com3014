package com.surrey.com3014.group5.websockets.dto;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;

/**
 * Data transfer object related to Active User.
 *
 * @author Spyridon Balkonis
 */
public class ActiveUserDTO extends UserDTO {

    private static final long serialVersionUID = -612202647015925422L;
    /**
     * Elo rating.
     */
    private double rating;

    /**
     * Initialise activeUser with userID.
     *
     * @param userId of this activeUser
     */
    public ActiveUserDTO(long userId) {
        super.setId(userId);
    }

    /**
     * Initialise a new activeUser from User model
     *
     * @param user a User model
     */
    public ActiveUserDTO(User user) {
        super(user);
        this.rating = user.getLeaderboard().getRating();
    }

    /**
     * Get elo rating.
     *
     * @return elo rating.
     */
    public double getRating() {
        return (double) Math.round(this.rating * 100.0) / 100.0;
    }

    /**
     * Set elo rating.
     *
     * @param rating elo rating.
     */
    public void setRating(double rating) {
        this.rating = rating;
    }
}
