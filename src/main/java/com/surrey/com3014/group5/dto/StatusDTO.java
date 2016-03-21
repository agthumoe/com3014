package com.surrey.com3014.group5.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author Aung Thu Moe
 */
@ApiModel
public class StatusDTO implements Serializable {
    public static final String ONLINE = "ONLINE";
    public static final String OFFLINE = "OFFLINE";
    public static final String AWAY = "AWAY";
    private static final long serialVersionUID = -5323944330114798910L;

    @ApiModelProperty(value = "Username of the user")
    private String username;
    @ApiModelProperty(value = "Current user status", allowableValues = ONLINE + "," + OFFLINE + "," + AWAY, position = 1)
    private String status;

    public StatusDTO() {
        super();
    }

    public StatusDTO(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusDTO{" +
            "username='" + username + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
