package com.surrey.com3014.group5.controllers.rest;

import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.users.ManagedUserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring MVC controller to handle user registration and management.
 *
 * @author Aung Thu Moe
 * @author Spyros Balkonis
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Delete the user given the id
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorDTO(HttpStatus.UNAUTHORIZED, "You are not authorized to access that page"), HttpStatus.UNAUTHORIZED);
        }
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
        List<ManagedUserDTO> managedUsers = users.stream().map(ManagedUserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(managedUsers);
    }
}
