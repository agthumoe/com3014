package com.surrey.com3014.group5.websockets.domains;

import java.io.Serializable;

/**
 * Access time associated with the user.
 *
 * @author Aung Thu Moe
 */
public class AccessTime implements Serializable {
    private static final long serialVersionUID = -5031593641962742363L;

    /**
     * Last access time of the associated user.
     */
    private long lastAccess;

    /**
     * Default constructor.
     */
    public AccessTime() {
        super();
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * Update the last access time.
     */
    public void update() {
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * Get last access time.
     *
     * @return last access time of the associated user.
     */
    public long getLastAccess() {
        return this.lastAccess;
    }
}
