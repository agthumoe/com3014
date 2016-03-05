package com.surrey.com3014.group5.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Spring MVC model to represent user
 */
@Entity
@Table(name="USER")
public class User extends AMutableModel{
	private static final long serialVersionUID = -2664947283441061553L;

	@Column(unique = true, nullable = false, updatable = false)
    @NotBlank(message = "Username must not be null or empty")
    private String username;

    @Column(unique = false, nullable = false)
    @NotNull(message = "Password must not be null or empty")
    @JsonIgnore
    private String password;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email must not be null or empty")
    private String email;

    @Column(unique = false, nullable = false)
    @NotBlank(message = "Name must not be null or empty")
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
