package com.surrey.com3014.group5.dto.users;

/**
 * @author Aung Thu Moe
 */
public class GamerDTO extends UserDTO {
    private static final long serialVersionUID = -1223709935605109559L;
    private Resolution resolution = new Resolution();
    private GameData gameData = new GameData();

    public GamerDTO() {
        super();
    }

    public GamerDTO(UserDTO user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setEmail(user.getEmail());
        setName(user.getName());
    }

    public int getHeight() {
        return this.resolution.getHeight();
    }

    public void setHeight(int height) {
        this.resolution.setHeight(height);
    }

    public int getWidth() {
        return this.resolution.getWidth();
    }

    public void setWidth(int width) {
        this.resolution.setWidth(width);
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
            ", resolution=" + resolution +
            ", gameData=" + gameData +
            '}';
    }

    private final class Resolution {
        private int height;
        private int width;

        private int getHeight() {
            return height;
        }

        private void setHeight(int height) {
            this.height = height;
        }

        private int getWidth() {
            return width;
        }

        private void setWidth(int width) {
            this.width = width;
        }

        @Override
        public String toString() {
            return "Resolution{" +
                "height=" + height +
                ", width=" + width +
                '}';
        }
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
