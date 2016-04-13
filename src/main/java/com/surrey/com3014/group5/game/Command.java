package com.surrey.com3014.group5.game;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
public class Command implements Serializable {
    private static final long serialVersionUID = 2367277263792938674L;

    public static final String NEW = "CHALLENGE.NEW";
    public static final String ACCEPT = "CHALLENGE.ACCEPT";
    public static final String DENY = "CHALLENGE.DECLINE";
    public static final String EXPIRED = "CHALLENGE.TIMEOUT";
    public static final String READY = "GAME.READY";
    public static final String PREP = "GAME.PREP";
    public static final String UPDATE = "GAME.UPDATE";
    public static final String FINISHED = "GAME.FINISHED";
    public static final String PING = "GAME.PREP_RECEIVED";
    public static final String START = "GAME.START";

    private String command;

    private JSONObject parsedData;

    public Command(String message) {
        JSONObject o = new JSONObject(message);
        this.parsedData = o.getJSONObject("data");
        this.command = o.getString("command");
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getStringData(String key) {
        return this.parsedData.getString(key);
    }

    public boolean getBooleanData(String key) {
        return this.parsedData.getBoolean(key);
    }

    public int getIntegerData(String key) {
        return this.parsedData.getInt(key);
    }

    public double getDoubleData(String key) {
        return this.parsedData.getDouble(key);
    }

    @Override
    public String toString() {
        return "Command{" +
            "command='" + command + '\'' +
            "parsedData='" + parsedData.toString() + '\'' +
            '}';
    }
}
