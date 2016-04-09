package com.surrey.com3014.group5.controllers.api;

import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.users.ManagedUserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.user.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
@Api(value = "User", description = "Operation about user", consumes = "application/json")
public class UserResource {

    @Autowired
    private UserService userService;

    /**
     * Get by username or email
     */
    @ApiOperation(value = "Filter by username or email", notes = "This can only be done by logged in user with ADMIN permission")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = ManagedUserDTO.class),
        @ApiResponse(code = 401, message = "You are not authorized to access this resource", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "The requested user with the provided information does not exist", response = ErrorDTO.class)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/filter")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> filteredBy(
        @ApiParam(value = "username to be filtered by", required = false) @RequestParam(value = "username", required = false) String username,
        @ApiParam(value = "email to be filtered by", required = false) @RequestParam(value = "email", required = false) String email) {
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

    /**
     * Delete the user given the id
     */
    @ApiOperation(value = "Delete specific user", notes = "This can only be done by logged in user with ADMIN permission")
    @ApiResponses(
        value = {
            @ApiResponse(code = 204, message = "User has been deleted"),
            @ApiResponse(code = 401, message = "You are not authorized to access this resource", response = ErrorDTO.class),
            @ApiResponse(code = 404, message = "The requested user with the provided information does not exist", response = ErrorDTO.class)
        })
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(
        @ApiParam(value = "User id to be deleted", required = true) @PathVariable("id") long id) {
        if (!SecurityUtils.isAuthenticated()) {
            return new ResponseEntity<>(new ErrorDTO(HttpStatus.UNAUTHORIZED, "You are not authorized to access this resource"), HttpStatus.UNAUTHORIZED);
        }
        Optional<User> maybeUser = userService.findOne(id);
        if (maybeUser.isPresent()) {
            userService.delete(maybeUser.get());
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.NOT_FOUND, "The requested user with the provided information does not exist"), HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Get all user", notes = "This can only be done by logged in user with ADMIN permission")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = ManagedUserDTO.class, responseContainer = "List"),
        @ApiResponse(code = 401, message = "You are not authorized to access this resource", response = ErrorDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "Page number", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "size", value = "Size of result", dataType = "int", paramType = "query"),
        @ApiImplicitParam(name = "sort", value = "Field name to be sorted by", dataType = "string", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAll(@ApiIgnore Pageable pageRequest) {
        Page<User> users = userService.getUsers(pageRequest);
        Sort sort;
        List<ManagedUserDTO> managedUserDTOs = new ArrayList<>();
        for (User user: users) {
            managedUserDTOs.add(new ManagedUserDTO(user));
        }
        return ResponseEntity.ok(managedUserDTOs);
    }
}
