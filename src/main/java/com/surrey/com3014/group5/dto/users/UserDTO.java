package com.surrey.com3014.group5.dto.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.surrey.com3014.group5.models.impl.User;
import io.swagger.annotations.ApiModel;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Data transfer object for user information.
 *
 * @author Aung Thu Moe
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3180494648509067715L;
    /**
     * Unique id of this user.
     */
    private long id;

    /**
     * Username of this user.
     */
    @Pattern(regexp = "^[a-z0-9]*$")
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    /**
     * Email of this user.
     */
    @Email
    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

    /**
     * Display name of this user.
     */
    @Size(max = 100)
    private String name;

    /**
     * Default constructor.
     */
    public UserDTO() {
        super();
    }

    /**
     * Initialise new UserDTO from User model
     *
     * @param user User model
     */
    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    /**
     * Get unique user's id.
     *
     * @return unique user's id
     */
    public long getId() {
        return id;
    }

    /**
     * Set user's id.
     *
     * @param id unique id of this user.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for username.
     *
     * @return username of this user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for username.
     *
     * @param username of this user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for email
     *
     * @return email of this user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for email
     *
     * @param email of this user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for name.
     *
     * @return name of this user.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     *
     * @param name of this user.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return id == userDTO.id;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}

