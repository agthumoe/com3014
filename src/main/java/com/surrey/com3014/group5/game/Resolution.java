package com.surrey.com3014.group5.game;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
public class Resolution implements Serializable {
    private static final long serialVersionUID = -7610151504517214517L;
    private int height;
    private int width;

    public Resolution(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public Resolution() {
        super();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
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
