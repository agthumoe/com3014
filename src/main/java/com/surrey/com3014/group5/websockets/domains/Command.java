package com.surrey.com3014.group5.websockets.domains;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * This class represents websocket command. This class translate the JSON format string to the command
 * object to be handled.
 *
 * @author Aung Thu Moe
 */
public class Command implements Serializable {
    private static final long serialVersionUID = 2367277263792938674L;
    /**
     * Command extracted from the parsed json string.
     */
    private String command;
    /**
     * Parsed json object from the input json string.
     */
    private JSONObject parsedData;

    /**
     * Initialise a new command object with json string message. JSON string must contain two fields,
     * data field and command fields. data field may contains other attributes.
     *
     * @param message The specified json string message.
     */
    public Command(String message) {
        JSONObject o = new JSONObject(message);
        this.parsedData = o.getJSONObject("data");
        this.command = o.getString("command");
    }

    /**
     * Get the command specified.
     *
     * @return command specified.
     */
    public String getCommand() {
        return command;
    }

    /**
     * Set the command
     *
     * @param command specified.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Get string data from parsed data json object.
     *
     * @param key to be extracted.
     * @return string data extracted using the specified key.
     */
    public String getStringData(String key) {
        return this.parsedData.getString(key);
    }

    /**
     * Get boolean data from parsed data json object.
     *
     * @param key to be extracted.
     * @return boolean data extracted using the spcified key.
     */
    public boolean getBooleanData(String key) {
        return this.parsedData.getBoolean(key);
    }

    /**
     * Get integer data from parsed data json object.
     *
     * @param key to be extracted.
     * @return integer data extracted using the specified key.
     */
    public int getIntegerData(String key) {
        return this.parsedData.getInt(key);
    }

    /**
     * Get double data from parsed data json object.
     *
     * @param key to be extracted.
     * @return double data extracted using the specified key.
     */
    public double getDoubleData(String key) {
        return this.parsedData.getDouble(key);
    }

    /**
     * Get parsed data json object.
     *
     * @return data json object.
     */
    public JSONObject getData() {
        return this.parsedData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Command{" +
            "command='" + command + '\'' +
            "parsedData='" + parsedData.toString() + '\'' +
            '}';
    }

    /**
     * This static class holds the commands related to the game challenge.
     */
    public static final class Challenge {
        public static final String NEW = "CHALLENGE.NEW";
        public static final String ACCEPT = "CHALLENGE.ACCEPT";
        public static final String DECLINE = "CHALLENGE.DECLINE";
        public static final String TIMEOUT = "CHALLENGE.TIMEOUT";
    }

    /**
     * This static class holds the commands related to the game protocol.
     */
    public static final class Game {
        public static final String LOADED = "GAME.LOADED";
        public static final String PING = "GAME.PING";
        public static final String PONG = "GAME.PONG";
        public static final String PREP = "GAME.PREP";
        public static final String READY = "GAME.READY";
        public static final String FINISHED = "GAME.FINISHED";
        public static final String START = "GAME.START";
        public static final String UPDATE = "GAME.UPDATE";
        public static final String STARTED = "GAME.STARTED";
    }

    /**
     * This static class holds the commands related to error message.
     */
    public static final class Error {
        public static final String DENY = "ERROR.WEB_SOCKET.DENY";
        public static final String EXPIRED = "ERROR.GAME.EXPIRED";
    }
}
