package com.surrey.com3014.group5.websockets.domains;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
public class AccessTime implements Serializable {
    private static final long serialVersionUID = -5031593641962742363L;

    private long lastAccess;

    public AccessTime() {
        super();
        this.lastAccess = System.currentTimeMillis();
    }

    public void update() {
        this.lastAccess = System.currentTimeMillis();
    }

    public long getLastAccess() {
        return this.lastAccess;
    }
}
