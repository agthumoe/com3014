package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

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

    @Autowired
    private AuthorityService authorityService;

    @ModelAttribute("user")
    public User setupUser(){
        return new User();
    }

    /**
     * Creating new user.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public User create(@ModelAttribute("user") User user, HttpServletResponse response) {
        user = userService.create(user);
        LOGGER.debug("user created -> " + user.toString());
        response.setHeader("Location", "/users/" + user.getId());
        return user;
    }

    /**
     * Get by id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User read(@PathVariable("id") long id) {
        Optional<User> user = userService.findOne(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundException("The requested user with username does not exist");
    }

    /**
     * Get by username or email
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User read(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email) {
        Optional<User> user = Optional.empty();
        if (!(username == null || "".equals(username.trim()))) {
            user = userService.findByUsername(username);
        }
        if (!(email == null || "".equals(email.trim()))) {
            user = userService.findByEmail(email);
        }
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundException("The requested user does not exist");
    }

    /**
     * Delete the user given the id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void delete(@PathVariable("id") long id, HttpServletResponse response) {
        Optional<User> userOptional = userService.findOne(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            response.setHeader("Location", "/api/v1/users");
            userService.delete(user);
        }

        throw new NotFoundException("The requested user does not exist");
    }

    /**
     * Update the user
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void update(@PathVariable("id") long id, String password, String email, String name) {
        Optional<User> userOptional = userService.findOne(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(password);
            user.setEmail(email);
            user.setName(name);
            userService.update(user);
        }
        throw new NotFoundException("The requested user does not exist");
    }

}
