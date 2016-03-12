package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @ModelAttribute("user")
    public User setupUser(){
        return new User();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public User register(@ModelAttribute("user") User user, HttpServletResponse response) {
        Optional<Authority> userAuthority = authorityService.findByType("user");
        if (!userAuthority.isPresent()) {
            throw new NullPointerException("user authority does not exist");
        }
        // clear any predefined authority from user request
        user.getAuthorities().clear();
        // set to user authority as default
        user.addAuthority(userAuthority.get());
        user = userService.create(user);
        LOGGER.debug("user created -> " + user.toString());
        response.setHeader("Location", "/api/v1/users/" + user.getId());
        return user;
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
    public String isAuthenticated(HttpServletRequest request) {
        LOGGER.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getAccount() {
        Optional<User> userOptional = userService.getUserWithAuthorities();
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(new UserDTO(userOptional.get()));
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
