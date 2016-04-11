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
 * @author Aung Thu Moe
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ApiModel
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3180494648509067715L;

    private long id;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    @Email
    @NotBlank
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 100)
    private String name;

    public UserDTO() {
        super();
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    public UserDTO(long id, String username, String email, String name) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        if (id != userDTO.id) return false;
        if (!username.equals(userDTO.username)) return false;
        return email.equals(userDTO.email);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + username.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

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

