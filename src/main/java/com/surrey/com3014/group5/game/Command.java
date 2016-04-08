package com.surrey.com3014.group5.game;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
public class Command implements Serializable {
    private static final long serialVersionUID = 2367277263792938674L;

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
}
