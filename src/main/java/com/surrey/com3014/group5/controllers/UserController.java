package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.models.User;
import com.surrey.com3014.group5.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

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

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService){
        this.userService = userService;
    }

    @ModelAttribute("user")
    public User setupUser(){
        return new User();
    }

    /**
     * Creating new user.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public User create(@ModelAttribute("user") User user) {
        user = userService.create(user);
        LOGGER.debug("user created -> " + user.toString());
        return user;
    }

    /**
     * Delete the user given the id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteById")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteById(long id) {
        User user = userService.findOne(id);
        userService.delete(user);
    }

    /**
     * Delete the user given the username
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteByUsername")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteByUsername(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("Entity with username: " + username + " not found");
        }
        userService.delete(user);
    }

    /**
     * Update the user
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(long id, String password, String email, String name) {
        User user = userService.findOne(id);
        //String encodedPassword = getPasswordEncoder().encode(password);
        user.setPassword(password);
        user.setEmail(email);
        user.setName(name);
        userService.update(user);
    }

    /**
     * Get by username
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getByUsername")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User getByUsername(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("Entity with username: " + username + " not found");
        }
        return user;
    }

    /**
     * Get by email
     */
    @RequestMapping("/getByEmail")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public User getByEmail(String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("Entity with email: " + email + "not found");
        }
        return user;
    }

    public IUserService getUserService() {
        return userService;
    }
}
