package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.game.Resolution;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
public class GamerDTO extends UserDTO {
//    private static final Logger LOGGEr = LoggerFactory.getLogger(GamerDTO.class);
    public static final String CHALLENGER = "CHALLENGER";
    public static final String CHALLENGED = "CHALLENGED";
    private static final long serialVersionUID = -1223709935605109559L;
    private String role;
    private Optional<Resolution> resolution = Optional.empty();
    private boolean ready = false;
    private GameData gameData = new GameData();
    private long messageSentTime;

    private long messageReceivedTime;

    public GamerDTO() {
        super();
    }

    public GamerDTO(UserDTO user, String role) {
        setId(user.getId());
        setUsername(user.getUsername());
        setEmail(user.getEmail());
        setName(user.getName());
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
//        LOGGEr.debug("UserID: {} set resolution", getId());
        Assert.notNull(resolution);
        if (this.resolution.isPresent()) {
            throw new RuntimeException("resolution is already set");
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
        return "Gamer{" +
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
