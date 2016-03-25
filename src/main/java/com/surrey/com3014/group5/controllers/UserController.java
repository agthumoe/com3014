package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.configs.SecurityConfig;
import com.surrey.com3014.group5.dto.ManagedUserDTO;
import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private LeaderboardService leaderboardService;

    @ModelAttribute("user")
    public User setupUser() {
        return new User();
    }

    /**
     * Creating new user.
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) throws URISyntaxException {
        Optional<User> maybeUser = userService.findByUsername(userDTO.getUsername());
        if (maybeUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST, "username already registered"));
        }
        maybeUser = userService.findByEmail(userDTO.getEmail());
        if (maybeUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST, "email already registered"));
        }
        User user = userService.createUserWithAuthorities(userDTO);
        Leaderboard leaderboard = new Leaderboard(user);
        leaderboardService.create(leaderboard);
        LOGGER.debug("new user created with the provided authority -> " + user.toString());
        return ResponseEntity.created(new URI("/api/users/" + user.getId())).body(new ManagedUserDTO(user));
    }

    /**
     * Get by id
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> read(@PathVariable("id") long id) {
        Optional<User> maybeUser = userService.findOne(id);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            user.getAuthorities().size();
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST, "User with the requested id does not exist"));
    }

    /**
     * Update the user
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public ResponseEntity<?> update(@RequestBody ManagedUserDTO managedUserDTO) {
        Optional<User> existingUser = userService.findByEmail(managedUserDTO.getEmail());
        if (existingUser.isPresent() && (existingUser.get().getId() != managedUserDTO.getId())) {
            return ResponseEntity.badRequest().body(new ErrorDTO(HttpStatus.BAD_REQUEST, "email already registered"));
        }

        Optional<User> maybeUser = userService.findOne(managedUserDTO.getId());
        if (!maybeUser.isPresent()) {
            return new ResponseEntity<>("User with the provided ID does not exist", HttpStatus.NOT_FOUND);
        }
        User user = maybeUser.get();
        user.setEmail(managedUserDTO.getEmail());
        user.setName(managedUserDTO.getName());
        user.getAuthorities().clear();
        for (String authority: managedUserDTO.getAuthorities()) {
            Optional<Authority> authorityOptional = authorityService.findByType(authority);
            if (!authorityOptional.isPresent()) {
                throw new NotFoundException("Authority provided does not exist in the system");
            }
            user.addAuthority(authorityOptional.get());
        }
        if (user.getAuthorities().size() == 0) { // to make sure every user has at least USER access
            user.addAuthority(authorityService.findByType(SecurityConfig.USER).get());
        }
        user = userService.update(user);
        return ResponseEntity.ok(new ManagedUserDTO(user));
    }

    /**
     * Delete the user given the id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        Optional<User> maybeUser = userService.findOne(id);
        if (maybeUser.isPresent()) {
            userService.delete(maybeUser.get());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.NOT_FOUND, "The requested user with the provided information does not exist"), HttpStatus.NOT_FOUND);
    }

    /**
     * Get by username or email
     */
    @RequestMapping(method = RequestMethod.GET, value = "/filter")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> filteredBy(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email) {
        Optional<User> maybeUser = userService.findByUsername(username);
        User user = null;
        if (maybeUser.isPresent()) {
            user = maybeUser.get();
            user.getAuthorities().size();
            return ResponseEntity.ok(new ManagedUserDTO(user));
        }
        maybeUser = userService.findByEmail(email);
        if (maybeUser.isPresent()) {
            user = maybeUser.get();
            user.getAuthorities().size();
            return ResponseEntity.ok(new ManagedUserDTO(user));
        }
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.NOT_FOUND, "The requested user with the provided information does not exist"), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAll() {
        List<User> users = userService.getAll();
        List<ManagedUserDTO> managedUsers = new ArrayList<>();
        for (User user : users) {
            managedUsers.add(new ManagedUserDTO(user));
        }
        return ResponseEntity.ok(managedUsers);
    }
}
