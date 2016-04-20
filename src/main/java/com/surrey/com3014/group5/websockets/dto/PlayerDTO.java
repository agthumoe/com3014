package com.surrey.com3014.group5.websockets.dto;

import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.websockets.domains.EloRating;
import com.surrey.com3014.group5.websockets.domains.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
public class PlayerDTO extends UserDTO implements EloRating {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerDTO.class);
    public static final String CHALLENGER = "CHALLENGER";
    public static final String CHALLENGED = "CHALLENGED";
    private static final long serialVersionUID = -1223709935605109559L;
    private String role;
    private Optional<Resolution> resolution = Optional.empty();
    private boolean ready = false;
    private GameData gameData = new GameData();
    private double rating = 1500;
    private long messageSentTime;
    private long messageReceivedTime;

    public PlayerDTO(User user) {
        super(user);
        this.rating = user.getLeaderboard().getRating();
    }

    public PlayerDTO(User user, String role) {
        this(user);
        this.role = role;
        this.ready = false;
    }

    public long getMessageSentTime() {
        return messageSentTime;
    }

    public void setMessageSentTime(long messageSentTime) {
        this.messageSentTime = messageSentTime;
    }

    public long getMessageReceivedTime() {
        return messageReceivedTime;
    }

    public void setMessageReceivedTime(long messageReceivedTime) {
        this.messageReceivedTime = messageReceivedTime;
    }

    public long getPingRate() {
        return (this.messageReceivedTime - this.messageSentTime) / 2;
    }

    public Optional<Resolution> getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        Assert.notNull(resolution);
        if (this.resolution.isPresent()) {
            LOGGER.warn("Resolution has already set");
        }
        this.resolution = Optional.of(resolution);
    }

    public int getX() {
        return this.gameData.getX();
    }

    public void setX(int x) {
        this.gameData.setX(x);
    }

    public int getY() {
        return this.gameData.getY();
    }

    public void setY(int y) {
        this.gameData.setY(y);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRotation() {
        return this.gameData.getRotation();
    }

    public void setRotation(String rotation) {
        this.gameData.setRotation(rotation);
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", username=" + getUsername() +
            ", email=" + getEmail() +
            ", name=" + getName() +
            ", role=" + role +
            ", resolution=" + resolution.get() +
            ", gameData=" + gameData +
            ", ready=" + ready +
            '}';
    }

    @Override
    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public double getRating() {
        return this.rating;
    }

    @Override
    public long getUserId() {
        return getId();
    }

    private final class GameData {
        private int x;
        private int y;
        private String rotation;

        private int getX() {
            return x;
        }

        private void setX(int x) {
            this.x = x;
        }

        private int getY() {
            return y;
        }

        private void setY(int y) {
            this.y = y;
        }

        private String getRotation() {
            return rotation;
        }

        private void setRotation(String rotation) {
            this.rotation = rotation;
        }

        @Override
        public String toString() {
            return "GameData{" +
                "x=" + x +
                ", y=" + y +
                ", rotation='" + rotation + '\'' +
                '}';
        }
    }
}
