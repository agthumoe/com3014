package com.surrey.com3014.group5.controllers.api;

import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Spring Rest controller to query current account information.
 *
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api/account")
@Api(value = "Account", description = "Resource to request current user login", consumes = "application/json")
public class AccountResource {

    /**
     * Userservice to access user dao.
     */
    @Autowired
    private UserService userService;

    /**
     * Get current user login.
     *
     * @return current logged in user.
     */
    @ApiOperation(value = "Get current user login", notes = "This can only be done by logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = UserDTO.class),
        @ApiResponse(code = 401, message = "You are not authorised to access this resource", response = ErrorDTO.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> get() {
        Optional<User> currentUser = userService.getCurrentLogin();
        if (currentUser.isPresent()) {
            return ResponseEntity.ok(new UserDTO(currentUser.get()));
        }
        return new ResponseEntity<>(new ErrorDTO(HttpStatus.UNAUTHORIZED, "You are not authorised to access this resource"), HttpStatus.UNAUTHORIZED);
    }
}
