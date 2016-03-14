package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * Spring MVC controller to handle user registration and management.
 *
 * @author Aung Thu Moe
 * @author Spyros Balkonis
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public User setupUser() {
        return new User();
    }

    /**
     * Creating new user.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public void create(@ModelAttribute("user") UserDTO userDTO, HttpServletResponse response) {
        User user = userService.createUserWithAuthorities(userDTO);
        LOGGER.debug("user created with provided authority -> " + user.toString());
        response.setHeader("Location", "/users/" + user.getId());
    }

    /**
     * Get by id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public UserDTO read(@PathVariable("id") long id) {
        return new UserDTO(userService.findOne(id));
    }

    /**
     * Get by username or email
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public UserDTO read(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email) {
        if (!(username == null || "".equals(username.trim()))) {
            return new UserDTO(userService.findByUsername(username));
        }
        if (!(email == null || "".equals(email.trim()))) {
            return new UserDTO(userService.findByEmail(email));
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "The requested user with the provided information does not exist");
    }

    /**
     * Delete the user given the id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void delete(@PathVariable("id") long id, HttpServletResponse response) {
        User user = userService.findOne(id);
        response.setHeader("Location", "/api/users");
        userService.delete(user);
    }

    /**
     * Update the user
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void update(@PathVariable("id") long id, String password, String email, String name) {
        User user = userService.findOne(id);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        userService.update(user);
    }

}
