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
 *
 * @author Spyros Balkonis
 */
@Entity
@Table(name = "user")
public class User extends MutableModel {
    private static final long serialVersionUID = -2664947283441061553L;

    /**
     * User's username
     */
    @Pattern(regexp = "^[a-z0-9]*$")
    @NotBlank(message = "Username must not be null or empty")
    @Size(min = 1, max = 50)
    @Column(unique = true, nullable = false, updatable = false)
    private String username;

    /**
     * User's password
     */
    @JsonIgnore
    @NotBlank(message = "Password must not be null or empty")
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    @Column(nullable = false)
    private String password;

    /**
     * User's email
     */
    @Email
    @NotBlank(message = "Email must not be null or empty")
    @Size(min = 5, max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * User's name
     */
    @Size(max = 100)
    private String name;

    /**
     * User account status. If enabled is false, user cannot log in
     * or use the account.
     */
    @Column(nullable = false)
    private boolean enabled = false;

    /**
     * Authorities of this user.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "permission",
        joinColumns = {@JoinColumn(name = "userId", nullable = false, updatable = false)},
        inverseJoinColumns = {@JoinColumn(name = "authorityId", nullable = false, updatable = false)})
    private Set<Authority> authorities = new HashSet<>(0);

    /**
     * Leaderboard of this user.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Leaderboard leaderboard;

    /**
     * Default constructor initialise user object.
     */
    public User() {
        super();
    }

    /**
     * Constructor initialise user object.
     *
     * @param username User's username
     * @param password User's password
     * @param email    User's email
     * @param name     User's name
     * @param enabled  User account status
     */
    public User(String username, String password, String email, String name, boolean enabled) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.enabled = enabled;
    }

    /**
     * Get username of this user.
     *
     * @return username of this user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username of this user.
     *
     * @param username of this user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get status of this user.
     *
     * @return status of this user.
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Set status of this user.
     *
     * @param enabled status of this user.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Get encrypted password of this user.
     *
     * @return encrypted password of this user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the encrypted password of this user.
     *
     * @param password encrypted password of this user.
     *                 Parameter password should be already encrypted.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get email of this user.
     *
     * @return email of this user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set email of this user.
     *
     * @param email of this user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get name of this user.
     *
     * @return name of this user.
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of this user.
     *
     * @param name of this user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get leaderboard of this user.
     *
     * @return leaderboard of this user.
     */
    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    /**
     * Set leaderboard of this user.
     *
     * @param leaderboard of this user.
     */
    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    /**
     * Get authorities of this user.
     *
     * @return authorities of this user.
     */
    public Set<Authority> getAuthorities() {
        return this.authorities;
    }

    /**
     * Add new authority
     *
     * @param authority to be added
     */
    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "User{" +
            "id='" + getId() + '\'' +
            "createdDate='" + getCreatedDate() + '\'' +
            "lastModifiedDate='" + getLastModifiedDate() + '\'' +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
