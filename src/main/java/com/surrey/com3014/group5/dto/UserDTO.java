package com.surrey.com3014.group5.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aung Thu Moe
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel
public class UserDTO implements Serializable{

    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 60;
    private static final long serialVersionUID = 4730655544039668683L;

    @ApiModelProperty(value = "Username of the user", required = true)
    @Pattern(regexp = "^[a-z0-9]*$")
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @ApiModelProperty(value = "Password for user authentication (Password is only required in request body, it will not be exposed in the response body)", required = true, position = 1)
    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @ApiModelProperty(value = "Email of the user", required = true, position = 2)
    @Email
    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

    @ApiModelProperty(value = "Name of the user", position = 3)
    @Size(max = 100)
    private String name;

    @ApiModelProperty(value = "authorities of the user", position = 4, hidden = true)
    private List<String> authorities = new ArrayList<>();

    public UserDTO() {
        super();
    }

    public UserDTO (User user) {
        super();
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setUsername(user.getUsername());
        this.password = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(String authority) {
        this.authorities.add(authority);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", authorities=" + authorities +
            '}';
    }

    public static UserDTO createUserDTOWithAuthorities(User user) {
        UserDTO userDTO = new UserDTO(user);
        userDTO.setAuthorities(user.getAuthorities().stream().map(Authority::getAuthority).collect(Collectors.toList()));
        return userDTO;
    }
}
