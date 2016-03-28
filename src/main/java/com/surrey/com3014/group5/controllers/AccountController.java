package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.users.RegisterUserDTO;
import com.surrey.com3014.group5.dto.users.UpdatePasswordDTO;
import com.surrey.com3014.group5.dto.users.UpdateUserDTO;
import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.exceptions.BadRequestException;
import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 */
@Controller
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @Resource(name = "userAuthority")
    private Authority userAuthority;

    @Resource(name = "duplicateUsernameValidator")
    private Validator duplicateUsernameValidator;

    @Resource(name = "duplicateEmailValidator")
    private Validator duplicateEmailValidator;

    @Resource(name = "currentPasswordValidator")
    private Validator currentPasswordValidator;

    @Resource(name = "confirmPasswordValidator")
    private Validator confirmPasswordValidator;

    @ModelAttribute("registerUserDTO")
    public RegisterUserDTO getRegisterUserDTO() {
        return new RegisterUserDTO();
    }

    @ModelAttribute("updateUserDTO")
    public UpdateUserDTO getUpdateUserDTO() {
        return new UpdateUserDTO();
    }

    @ModelAttribute("updatePasswordDTO")
    public UpdatePasswordDTO getUpdatePasswordDTO() {
        return new UpdatePasswordDTO();
    }

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

    @RequestMapping(value = "/account/register", method = RequestMethod.GET)
    public String getRegistrationForm(Model model) {
        if (SecurityUtils.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("title", "User Registration");
        model.addAttribute("description", "This is users registration page for Tron game");
        return "account/register";
    }

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
            user.addAuthority(userAuthority);
            userService.create(user);
            LOGGER.debug("New users registered: " + registerUserDTO.toString());
            return "redirect:/index";
        }
    }

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
            SecurityUtils.getCurrentUsername().equals(user.getUsername()))){
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
            LOGGER.debug("User: " + user.getId() + ", has been updated");
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/account/{id}/password")
    public String getUpdatePasswordPage(@PathVariable("id") long id, Model model) {
        Optional<User> maybeUser = this.userService.findOne(id);
        if (!maybeUser.isPresent()) {
            throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
        }
        model.addAttribute("id", id);
        return "account/update_password";
    }

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
            SecurityUtils.getCurrentUsername().equals(user.getUsername()))){
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
            LOGGER.debug("Password of User {id: " + id + "} has been updated");
            return "redirect:/account/" + id;
        }
    }

}
