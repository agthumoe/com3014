package com.surrey.com3014.group5.controllers.api;

import com.surrey.com3014.group5.dto.PagedListDTO;
import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.users.ManagedUserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.user.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring Rest controller to handle user registration and management.
 *
 * @author Aung Thu Moe
 * @author Spyros Balkonis
 */
@RestController
@RequestMapping("/api/users")
@Api(value = "User", description = "Operation about user", consumes = "application/json")
public class UserResource {
    /**
     * Userservice to access user dao.
     */
    @Autowired
    private UserService userService;

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

    /**
     * Get all user.
     *
     * @param pageRequest pagination information.
     * @return all users satisfing the provided pageRequest.
     */
    @ApiOperation(value = "Get all user", notes = "This can only be done by logged in user with ADMIN permission")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = PagedListDTO.class),
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
        Page<User> pages = userService.getPagedList(pageRequest);
        if (pages.getNumber() >= pages.getTotalPages()) {
            pages = userService.getPagedList(new PageRequest(pages.getTotalPages() - 1, pageRequest.getPageSize(), pageRequest.getSort()));
        }
        PagedListDTO<ManagedUserDTO> pagedListDTO = new PagedListDTO<>();
        List<ManagedUserDTO> list = new ArrayList<>();
        pages.forEach(user -> list.add(new ManagedUserDTO(user)));
        pagedListDTO.setPagedList(list);
        pagedListDTO.setPageSize(pageRequest.getPageSize());
        pagedListDTO.setPageNumber(pageRequest.getPageNumber());
        pagedListDTO.setNumberOfElements(userService.count());
        pagedListDTO.setTotalPages(pages.getTotalPages());
        return ResponseEntity.ok(pagedListDTO);
    }

    /**
     * Filter users by the provided filter method.
     *
     * @param filterBy column name to be used to filter the users.
     * @param filter   value to filter the users.
     * @param limit    number of results.
     * @return List of users satisfing the provided filters information.
     */
    @ApiOperation(value = "Filter by username, email, name, status or role", notes = "This can only be done by logged in user with ADMIN permission")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = ManagedUserDTO.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Invalid query", response = ErrorDTO.class),
        @ApiResponse(code = 401, message = "You are not authorized to access this resource", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "The requested user with the provided information does not exist", response = ErrorDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "filterBy", value = "Column name to be filtered by", dataType = "string", paramType = "query", allowableValues = "[username,email,name,enabled,role]"),
        @ApiImplicitParam(name = "filter", value = "Query string to be filtered", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "limit", value = "Number of results to be limited", dataType = "int", paramType = "query")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/filter")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> filter(
        @RequestParam(value = "filterBy") String filterBy,
        @RequestParam(value = "filter") String filter,
        @RequestParam(value = "limit", required = false, defaultValue = "10") long limit) {
        filterBy = filterBy.toLowerCase().trim();
        List<User> users = new ArrayList<>();
        if (filterBy.equals("username")) {
            users = userService.findByUsernameContaining(filter);
        } else if (filterBy.equals("email")) {
            users = userService.findByEmailContaining(filter);
        } else if (filterBy.equals("name")) {
            users = userService.findByNameContaining(filter);
        } else if (filterBy.equals("enabled")) {
            return filterByEnabled(filter, limit);
        } else if (filterBy.equals("role")) {
            return filterByRole(filter, limit);
        }
        List<ManagedUserDTO> managedUserDTOs = new ArrayList<>();
        for (int i = 0; i < users.size() && i < limit; i++) {
            managedUserDTOs.add(new ManagedUserDTO(users.get(i)));
        }
        return ResponseEntity.ok(managedUserDTOs);
    }

    /**
     * Filter by role.
     *
     * @param filter specific query provied by user.
     * @param limit  number of results
     * @return List of users filtered by role.
     */
    private ResponseEntity<?> filterByRole(String filter, long limit) {
        boolean isAdmin;
        if (filter.equals("admin")) {
            isAdmin = true;
        } else if (filter.equals("user")) {
            isAdmin = false;
        } else {
            return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, "Invalid query: " + filter), HttpStatus.BAD_REQUEST);
        }
        List<User> users = userService.getAll();
        List<ManagedUserDTO> managedUserDTOs = new ArrayList<>();
        for (User user : users) {
            ManagedUserDTO managedUserDTO = new ManagedUserDTO(user);
            if (managedUserDTO.isAdmin() == isAdmin && managedUserDTOs.size() < limit) {
                managedUserDTOs.add(managedUserDTO);
                if (managedUserDTOs.size() >= limit) {
                    break;
                }
            }
        }
        return ResponseEntity.ok(managedUserDTOs);
    }

    /**
     * Filter by enabled (status).
     *
     * @param filter specific query provided by user.
     * @param limit  number of results.
     * @return list of users filtered by enabled (status).
     */
    private ResponseEntity<?> filterByEnabled(String filter, long limit) {
        List<User> users;
        if (filter.equals("true") || filter.equals("1") || filter.equals("yes")) {
            users = userService.findByEnabled(true);
        } else if (filter.equals("false") || filter.equals("0") || filter.equals("no")) {
            users = userService.findByEnabled(false);
        } else {
            return new ResponseEntity<>(new ErrorDTO(HttpStatus.BAD_REQUEST, "Invalid query: " + filter), HttpStatus.BAD_REQUEST);
        }
        List<ManagedUserDTO> managedUserDTOs = new ArrayList<>();
        for (int i = 0; i < users.size() && i < limit; i++) {
            managedUserDTOs.add(new ManagedUserDTO(users.get(i)));
        }
        return ResponseEntity.ok(managedUserDTOs);
    }
}
