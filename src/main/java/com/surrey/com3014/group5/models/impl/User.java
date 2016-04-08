package com.surrey.com3014.group5.models.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surrey.com3014.group5.models.MutableModel;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static com.surrey.com3014.group5.dto.Credentials.PASSWORD_MAX_LENGTH;
import static com.surrey.com3014.group5.dto.Credentials.PASSWORD_MIN_LENGTH;

/**
 * Spring MVC model to represent users
 * @author Spyros Balkonis
 */
@Entity
@Table(name="user")
public class User extends MutableModel {
	private static final long serialVersionUID = -2664947283441061553L;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotBlank(message = "Username must not be null or empty")
    @Size(min = 1, max = 50)
	@Column(unique = true, nullable = false, updatable = false)
    private String username;

    @JsonIgnore
    @NotBlank(message = "Password must not be null or empty")
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    @Column(nullable = false)
    private String password;

    @Email
    @NotBlank(message = "Email must not be null or empty")
    @Size(min = 5, max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 100)
    private String name;

    @Column(nullable = false)
    private boolean enabled = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "permission",
        joinColumns = {@JoinColumn(name = "userId", nullable = false, updatable = false)},
        inverseJoinColumns = {@JoinColumn(name = "authorityId", nullable = false, updatable = false)})
    private Set<Authority> authorities = new HashSet<>(0);

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Leaderboard leaderboard;

    public User() {
        super();
    }

    public User(String username, String password, String email, String name, boolean enabled) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.enabled = enabled;
    }

    public User(long id, String username, String password, String email, String name, boolean enabled) {
        this(username, password, email, name, enabled);
        this.setId(id);
    }

    public User(long id, String username, String password, String email, String name, boolean enabled, final Set<Authority> authorities) {
        this(id, username, password, email, name, enabled);
        this.authorities = new HashSet<>(authorities);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Set<Authority> getAuthorities() {
        return this.authorities;
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
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
