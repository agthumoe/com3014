package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @ModelAttribute("user_dto")
    public UserDTO setupUser(){
        return new UserDTO();
    }

    @ApiOperation(value = "Register new user.", notes = "Create new user with default authority USER")
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public void register(@ModelAttribute("user_dto") UserDTO userDTO, HttpServletResponse response) {
        User user = userService.create(userDTO);
        LOGGER.debug("user created -> " + user.toString());
        response.setHeader("Location", "/api/users/" + user.getId());
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public UserDTO getAccount() {
        return new UserDTO(userService.getUserWithAuthorities());
    }
}
