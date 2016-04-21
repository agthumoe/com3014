package com.surrey.com3014.group5.websockets.domains;

import java.io.Serializable;

/**
 * Player's screen dimensions.
 *
 * @author Aung Thu Moe
 */
public class Resolution implements Serializable {
    /**
     * Amount of pixels to be reduced from both dimensions.
     */
    public static final int OFFSET = 40;
    private static final long serialVersionUID = -7610151504517214517L;
    /**
     * Height of the resultion.
     */
    private int height;
    /**
     * Width of the resolution.
     */
    private int width;

    /**
     * Initialise a resolution with the provided height and width
     *
     * @param height of the resolution.
     * @param width  of the resolution.
     */
    public Resolution(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Default constructor.
     */
    public Resolution() {
        super();
    }

    /**
     * Get the height of the resolution.
     *
     * @return height of the resoltuion.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set the height of the resolution.
     *
     * @param height of the resolution.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get width of the resolution.
     *
     * @return width of the resolution.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set width of the resolution.
     *
     * @param width of the resolution.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Resolution{" +
            "height=" + height +
            ", width=" + width +
            '}';
    }
}
