package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.models.User;
import com.surrey.com3014.group5.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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


    /**
     * Creating new user.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    @ResponseBody
    public String create(String username, String password, String email, String name) {
        String userId = "";
        try {
            User user = new User(username, password, email, name);
            user = userService.create(user);
            userId = String.valueOf(user.getId());
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User successfully created with id = " + userId;
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
