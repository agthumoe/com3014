package com.surrey.com3014.group5.dto.users;

import com.surrey.com3014.group5.game.Resolution;
import org.springframework.util.Assert;

/**
 * @author Aung Thu Moe
 */
public class GamerDTO extends UserDTO {
    private static final long serialVersionUID = -1223709935605109559L;
    public static final String CHALLENGER = "GAMER.CHALLENGER";
    public static final String CHALLENGED = "GAMER.CHALLENGED";

    private String role;
    private Resolution resolution = null;
    private GameData gameData = new GameData();

    public GamerDTO() {
        super();
    }

    public GamerDTO(UserDTO user, String role) {
        setId(user.getId());
        setUsername(user.getUsername());
        setEmail(user.getEmail());
        setName(user.getName());
        this.role = role;
    }

    public Resolution getResolution() {
        return resolution;
    }

    public void setResolution(Resolution resolution) {
        Assert.notNull(resolution);
        this.resolution = resolution;
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

    @Override
    public String toString() {
        return "Gamer{" +
            "id=" + getId() +
            ", username=" + getUsername() +
            ", email=" + getEmail() +
            ", name=" + getName() +
            ", role=" + role +
            ", resolution=" + resolution +
            ", gameData=" + gameData +
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
