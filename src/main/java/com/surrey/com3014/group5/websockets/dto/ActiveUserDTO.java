package com.surrey.com3014.group5.websockets.dto;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;

import java.text.DecimalFormat;

/**
 * @author Spyridon Balkonis
 */
public class ActiveUserDTO extends UserDTO {

    private static final long serialVersionUID = -2623220141304335028L;

    private double rating;

    public ActiveUserDTO(long userId) {
        super.setId(userId);
    }

    public ActiveUserDTO(User user) {
        super(user);
        this.rating = user.getLeaderboard().getRating();
    }

    public String getRating() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(this.rating);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
