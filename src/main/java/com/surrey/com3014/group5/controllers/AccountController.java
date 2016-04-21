package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.users.*;
import com.surrey.com3014.group5.exceptions.BadRequestException;
import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author Aung Thu Moe
 *         Spring mvc controller to handle account management.
 */
@Controller
public class AccountController {
    /**
     * Userservice to access user dao.
     */
    @Autowired
    private UserService userService;

    /**
     * AuthorityService to access authority dao.
     */
    @Autowired
    private AuthorityService authorityService;

    /**
     * To validate duplicated username.
     */
    @Resource(name = "duplicateUsernameValidator")
    private Validator duplicateUsernameValidator;

    /**
     * To validate duplicated email.
     */
    @Resource(name = "duplicateEmailValidator")
    private Validator duplicateEmailValidator;

    /**
     * To validate current login password.
     */
    @Resource(name = "currentPasswordValidator")
    private Validator currentPasswordValidator;

    /**
     * To validate confirm password is the same as current password.
     */
    @Resource(name = "confirmPasswordValidator")
    private Validator confirmPasswordValidator;

    /**
     * Initialise RegisterUserDTO to supply to the controller.
     *
     * @return new RegisterUserDTO object.
     */
    @ModelAttribute("registerUserDTO")
    public RegisterUserDTO getRegisterUserDTO() {
        return new RegisterUserDTO();
    }

    /**
     * Initialise UpdateUserDTO to supply to the controller.
     *
     * @return new UpdateUserDTO object.
     */
    @ModelAttribute("updateUserDTO")
    public UpdateUserDTO getUpdateUserDTO() {
        return new UpdateUserDTO();
    }

    /**
     * Initialise UpdatePasswordDTO to supply to the controller
     *
     * @return new UpdatePasswordDTO object.
     */
    @ModelAttribute("updatePasswordDTO")
    public UpdatePasswordDTO getUpdatePasswordDTO() {
        return new UpdatePasswordDTO();
    }

    /**
     * Get login page. Based on the different Httpstatus code provided,
     * notification message will be displayed based on the status code.<br>
     * Status code is also applied to the servlet response.
     *
     * @param status   HttpStatus code
     * @param logout   logout url
     * @param response HttpServletResponse to set status code.
     * @param model    Holder for model attributes.
     * @return login page
     */
    @RequestMapping(value = "/account/login")
    public String getLoginPage(
        @RequestParam(value = "status", required = false) Long status,
        @RequestParam(value = "logout", required = false) String logout,
        HttpServletResponse response,
        Model model) {
        if (status != null) {
            if (status == 404) {
                model.addAttribute("error", "Username does not exist");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            } else if (status == 403) {
                model.addAttribute("error", "Your account is currently disabled, please contact the administrator");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            } else if (status == 400) {
                model.addAttribute("error", "Incorrect password");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else if (status == 401) {
                model.addAttribute("error", "You are not authorized to access that page.<br>Please login to continue!");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                model.addAttribute("error", "Failed");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }

        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }
        return "account/login";
    }

    /**
     * Get registration form. Redirect to the last access page if user has
     * already logged in.
     *
     * @param model Holder for model attributes.
     * @return registraion form.
     */
    @RequestMapping(value = "/account/register", method = RequestMethod.GET)
    public String getRegistrationForm(Model model) {
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("title", "User Registration");
        model.addAttribute("description", "This is users registration page for Tron game");
        return "account/register";
    }

    /**
     * Process registration form queries.
     *
     * @param registerUserDTO Registration form data.
     * @return redirect to register page if there is any validation error,
     * otherwise, redirect to index page.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/account/register")
    public String register(
        @Validated @ModelAttribute("registerUserDTO") RegisterUserDTO registerUserDTO,
        BindingResult bindingResult
    ) {
        this.duplicateUsernameValidator.validate(registerUserDTO, bindingResult);
        this.duplicateEmailValidator.validate(registerUserDTO, bindingResult);
        this.confirmPasswordValidator.validate(registerUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "account/register";
        } else {
            User user = new User(registerUserDTO.getUsername(), registerUserDTO.getPassword(), registerUserDTO.getEmail(), registerUserDTO.getName(), true);
            user.addAuthority(authorityService.getUser());
            userService.create(user);
            return "redirect:/index";
        }
    }

    /**
     * Get user profile.
     *
     * @param id the requested user's id.
     * @return Get the requested user's profile
     * @throws BadRequestException       if user try to access other's profile.
     * @throws ResourceNotFoundException if user id does not exist.
     */
    @RequestMapping(value = "/account/{id}/profile", method = RequestMethod.GET)
    public String profile(@PathVariable("id") long id, Model model) {
        Optional<User> maybeUser = userService.findOne(id);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            // check users has the right authority to see the users info
            if (SecurityUtils.isAuthenticated() && user.getUsername().equals(SecurityUtils.getCurrentUsername())) {
                model.addAttribute("userDTO", new UserProfileDTO(user));
                return "account/profile";
            }
            throw new BadRequestException("You are not authorised to see this page");
        }
        throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
    }

    /**
     * Get the update page of the requested user profile.
     *
     * @param id the requested user's id.
     * @return Get the update page of the requested user profile.
     * @throws BadRequestException       if user try to access other's profile.
     * @throws ResourceNotFoundException if user id does not exist.
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") long id, Model model) {
        Optional<User> maybeUser = userService.findOne(id);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            // check users has the right authority to see the users info
            if (SecurityUtils.isAuthenticated() && user.getUsername().equals(SecurityUtils.getCurrentUsername())) {
                model.addAttribute("userDTO", new UserDTO(user));
                return "account/view";
            }
            throw new BadRequestException("You are not authorised to see this page");
        }
        throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
    }

    /**
     * Update the current user profile information.
     *
     * @param id            the requested user's id.
     * @param updateUserDTO Update form data.
     * @return redirect to update page if there is any validation error,
     * otherwise, redirect to index page.
     * @throws ResourceNotFoundException if user id does not exist.
     */
    @RequestMapping(method = RequestMethod.POST, value = "/account/{id}")
    public String update(
        @PathVariable("id") long id,
        @Validated @ModelAttribute("updateUserDTO") UpdateUserDTO updateUserDTO,
        BindingResult bindingResult,
        Model model
    ) {
        Optional<User> maybeUser = userService.findOne(id);
        if (!maybeUser.isPresent()) {
            throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
        }
        User user = maybeUser.get();
        if (!(SecurityUtils.isAuthenticated() &&
            SecurityUtils.getCurrentUsername().equals(user.getUsername()))) {
            throw new BadRequestException("You are not authorised to see this page");
        }

        this.currentPasswordValidator.validate(updateUserDTO, bindingResult);

        if (!user.getUsername().equals(updateUserDTO.getUsername())) {
            this.duplicateUsernameValidator.validate(updateUserDTO, bindingResult);
        }

        if (!user.getEmail().equals(updateUserDTO.getEmail())) {
            this.duplicateEmailValidator.validate(updateUserDTO, bindingResult);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("userDTO", new UserDTO(user));
            return "account/view";
        } else {
            user.setName(updateUserDTO.getName());
            user.setEmail(updateUserDTO.getEmail());
            userService.update(user);
            return "redirect:/index";
        }
    }

    /**
     * Get update password page.
     *
     * @param id the requested user's id.
     * @return update password page.
     * @throws ResourceNotFoundException if user id does not exist.
     */
    @RequestMapping(value = "/account/{id}/password")
    public String getUpdatePasswordPage(@PathVariable("id") long id, Model model) {
        Optional<User> maybeUser = this.userService.findOne(id);
        if (!maybeUser.isPresent()) {
            throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
        }
        model.addAttribute("id", id);
        return "account/update_password";
    }

    /**
     * Update password of the current user.
     *
     * @param id                the requested user's id.
     * @param updatePasswordDTO update password form data.
     * @return redirect to update password page if there is any validation error,
     * otherwise, redirect to user's profile.
     * @throws ResourceNotFoundException if user id does not exist.
     */
    @RequestMapping(value = "/account/{id}/password", method = RequestMethod.POST)
    public String updatePassword(
        @PathVariable("id") long id,
        @Validated @ModelAttribute("updatePasswordDTO") UpdatePasswordDTO updatePasswordDTO,
        BindingResult bindingResult) {
        Optional<User> maybeUser = this.userService.findOne(id);
        if (!maybeUser.isPresent()) {
            throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
        }

        User user = maybeUser.get();
        if (!(SecurityUtils.isAuthenticated() &&
            SecurityUtils.getCurrentUsername().equals(user.getUsername()))) {
            throw new BadRequestException("You are not authorised to see this page");
        }

        this.currentPasswordValidator.validate(updatePasswordDTO, bindingResult);
        this.confirmPasswordValidator.validate(updatePasswordDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "account/update_password";
        } else {
            user.setPassword(updatePasswordDTO.getPassword());
            this.userService.updatePassword(user);
            this.userService.update(user);
            return "redirect:/account/" + id;
        }
    }

}
