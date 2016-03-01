package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.models.User;
import com.surrey.com3014.group5.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Created by spyro on 21-Feb-16.
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
    @ResponseBody
    public ResponseEntity<String> create(@ModelAttribute("user") User user) {
        LOGGER.debug(user.toString());
        String userId = "";
        try {
            user = userService.create(user);
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.toString());
        }
        return ResponseEntity.ok(user.getUsername());
    }

    /**
     * Delete the user given the id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteById")
    @ResponseBody
    public String deleteById(long id) {
        try {
            User user = new User(id);
            userService.delete(user);
        }
        catch (Exception ex) {
            return "Error deleting the user:" + ex.toString();
        }
        return "User successfully deleted!";
    }

    /**
     * Delete the user given the username
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteByUsername")
    @ResponseBody
    public String deleteByUsername(String username) {
        try {
            User user = userService.findByUsername(username);
            userService.delete(user);
        }
        catch (Exception ex) {
            return "Error deleting the user:" + ex.toString();
        }
        return "User successfully deleted!";
    }

    /**
     *Update the user
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    @ResponseBody
    public String updateUser(long id, String password, String email, String name) {
        try {
            User user = userService.findOne(id);
            //String encodedPassword = getPasswordEncoder().encode(password);
            user.setPassword(password);
            user.setEmail(email);
            user.setName(name);
            userService.update(user);
        }
        catch (Exception ex) {
            return "Error updating the user: " + ex.toString();
        }
        return "User succesfully updated!";
    }

    /**
     * Get by username
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getByUsername")
    @ResponseBody
    public String getByUsername(String username) {
        String userId = "";
        try {
            User user = userService.findByUsername(username);
            userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + userId;
    }

    /**
     * Get by email
     */
    @RequestMapping("/getByEmail")
    @ResponseBody
    public String getByEmail(String email) {
        String userId = "";
        try {
            User user = userService.findByEmail(email);
            userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
            return "User not found";
        }
        return "The user id is: " + userId;
    }

    public IUserService getUserService() {
        return userService;
    }

}
