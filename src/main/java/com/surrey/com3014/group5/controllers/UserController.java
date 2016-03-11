package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

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
     * Delete the user given the id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteById(@PathVariable("id") long id, HttpServletResponse response) {
        Optional<User> userOptional = userService.findOne(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            response.setHeader("Location", "/users");
            userService.delete(user);
        }

        throw new NotFoundException("The requested user does not exist");
    }

    /**
     * Delete the user given the username
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteByUsername")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteByUsername(String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            userService.delete(user.get());
        }
        throw new NotFoundException("User with username: " + username + " does not exist");
    }

    /**
     * Update the user
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable("id") long id, String password, String email, String name) {
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

    /**
     * Get by username
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getByUsername")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User getByUsername(String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundException("The requested user with username: " + username + " does not exist");
    }

    /**
     * Get by email
     */
    @RequestMapping("/getByEmail")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User getByEmail(String email) {
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundException("The requested user with email: " + email + " does not exist");
    }

    public UserService getUserService() {
        return userService;
    }
}
