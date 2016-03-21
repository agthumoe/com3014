package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api")
@Api(value = "Account", description = "Manage current user account", consumes = "application/json")
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Register new user.", notes = "Create new user with default authority USER")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "created"),
        @ApiResponse(code = 400, message = "bad request")
    })
    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws URISyntaxException {
        Optional<User> maybeUser = userService.findByUsername(userDTO.getUsername());
        if (maybeUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST, "username already registered"));
        }
        maybeUser = userService.findByEmail(userDTO.getEmail());
        if (maybeUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST, "email already registered"));
        }
        User user = userService.create(userDTO);
        LOGGER.debug("user created -> " + user.toString());
        return ResponseEntity.created(new URI("/api/users/" + user.getId())).body(new UserDTO(user));
    }

    /**
     * GET  /account -> get the current login user.
     */
    @ApiOperation(value = "Get current login user", response = UserDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = UserDTO.class, message = "success"),
        @ApiResponse(code=401, message = "Unauthorized", response = ErrorDTO.class)
    })
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAccount() {
        Optional<User> maybeUser = userService.getCurrentLogin();
        if (maybeUser.isPresent()) {
            return ResponseEntity.ok(new UserDTO(maybeUser.get()));
        }
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.UNAUTHORIZED, "No user has been authenticated yet"), HttpStatus.UNAUTHORIZED);
    }
}
