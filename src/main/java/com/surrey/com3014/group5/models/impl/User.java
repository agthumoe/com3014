package com.surrey.com3014.group5.models.impl;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surrey.com3014.group5.models.AMutableModel;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
/**
 * Spring MVC model to represent user
 */
@Entity
@Table(name="USER")
public class User extends AMutableModel {
	private static final long serialVersionUID = -2664947283441061553L;

    @NotBlank(message = "Username must not be null or empty")
    @Pattern(regexp = "^[a-z0-9]*$")
    @Size(min = 1, max = 50)
	@Column(unique = true, nullable = false, updatable = false)
    private String username;

    @JsonIgnore
    @NotNull(message = "Password must not be null or empty")
    @Size(min = 60, max = 60)
    @Column(unique = false, nullable = false)
    private String password;

    @NotBlank(message = "Email must not be null or empty")
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Name must not be null or empty")
    @Size(max = 100)
    @Column(unique = false, nullable = false)
    private String name;

    public User() { }

    public User(long id) {
        this.setId(id);
    }

    public User(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
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

    @Override
    public String toString() {
        return "User{" +
            "id=" + this.getId() +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
