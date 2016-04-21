package com.surrey.com3014.group5.controllers;

import com.surrey.com3014.group5.dto.users.ManagedUserDTO;
import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import com.surrey.com3014.group5.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Spring mvc controller for user with admin role.
 *
 * @author Aung Thu Moe
 */
@Controller
public class AdminController {

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
     * To validate duplidated email.
     */
    @Resource(name = "duplicateEmailValidator")
    private Validator duplicateEmailValidator;

    /**
     * To validate confirm password is the same as current password.
     */
    @Resource(name = "confirmPasswordValidator")
    private Validator confirmPasswordValidator;

    /**
     * To validate password size. If password is null or blank, won't be validated.
     */
    @Resource(name = "nullablePasswordSizeValidator")
    private Validator nullablePasswordSizeValidator;

    /**
     * Initialise ManagedUserDTO to be handled by controller.
     *
     * @return new ManagedUserDTO object.
     */
    @ModelAttribute("managedUserDTO")
    public ManagedUserDTO getManagedUserDTO() {
        return new ManagedUserDTO();
    }

    /**
     * Get user administration (dashboard) page.
     *
     * @return user administration (dashboard) page.
     */
    @RequestMapping("/admin/users")
    public String index(Model model) {
        List<User> users = this.userService.getAll();
        int activeUsers = 0;
        int numberOfAdmins = 0;
        final Authority admin = authorityService.getAdmin();
        for (User user : users) {
            if (user.isEnabled()) {
                activeUsers++;
            }
            if (user.getAuthorities().contains(admin)) {
                numberOfAdmins++;
            }
        }
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("bannedUsers", users.size() - activeUsers);
        model.addAttribute("numberOfAdmins", numberOfAdmins);
        return "admin/index";
    }

    /**
     * Get new user creation page.
     *
     * @return new user creation page.
     */
    @RequestMapping("/admin/users/new")
    public String getNewUserPage() {
        return "admin/new";
    }

    /**
     * Create new user.
     *
     * @param managedUserDTO create user form data.
     * @return redirect to dashboard if successful, otherwise display validation
     * error message.
     */
    @RequestMapping(value = "/admin/users", method = RequestMethod.POST)
    public String create(
        @Validated @ModelAttribute("managedUserDTO") ManagedUserDTO managedUserDTO,
        BindingResult bindingResult) {
        this.duplicateUsernameValidator.validate(managedUserDTO, bindingResult);
        this.duplicateEmailValidator.validate(managedUserDTO, bindingResult);
        this.confirmPasswordValidator.validate(managedUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/new";
        } else {
            User user = new User(managedUserDTO.getUsername(), managedUserDTO.getPassword(), managedUserDTO.getEmail(), managedUserDTO.getName(), managedUserDTO.isEnabled());
            user.addAuthority(authorityService.getUser());
            if (managedUserDTO.isAdmin()) {
                user.addAuthority(authorityService.getAdmin());
            }
            userService.create(user);
            return "redirect:/admin/users";
        }
    }

    /**
     * View the specified user.
     *
     * @param id the requested user's id.
     * @return user information page.
     * @throws ResourceNotFoundException if user id does not exist.
     */
    @RequestMapping(value = "/admin/users/{id}")
    public String view(@PathVariable("id") long id, Model model) {
        Optional<User> maybeUser = userService.findOne(id);
        if (maybeUser.isPresent()) {
            model.addAttribute("managedUserDTO", new ManagedUserDTO(maybeUser.get()));
            return "admin/view";
        }
        throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
    }

    /**
     * Updated the specified user.
     *
     * @param id             the requested user's id.
     * @param managedUserDTO form data to be updated.
     * @return redirect to dashboard if successful, otherwise, displays validation
     * error message.
     * @throws ResourceNotFoundException if user id does not exist.
     */
    @RequestMapping(value = "/admin/users/{id}", method = RequestMethod.POST)
    public String update(
        @PathVariable("id") long id,
        @Validated @ModelAttribute("managedUserDTO") ManagedUserDTO managedUserDTO,
        BindingResult bindingResult) {
        Optional<User> maybeUser = userService.findOne(id);
        if (!maybeUser.isPresent()) {
            throw new ResourceNotFoundException("User (id: " + id + ") does not exist!");
        }

        User user = maybeUser.get();

        if (!user.getUsername().equals(managedUserDTO.getUsername())) {
            this.duplicateUsernameValidator.validate(managedUserDTO, bindingResult);
        }

        if (!user.getEmail().equals(managedUserDTO.getEmail())) {
            this.duplicateEmailValidator.validate(managedUserDTO, bindingResult);
        }

        this.nullablePasswordSizeValidator.validate(managedUserDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/view";
        } else {
            user.setEmail(managedUserDTO.getEmail());
            user.setName(managedUserDTO.getName());
            user.setEnabled(managedUserDTO.isEnabled());

            user.getAuthorities().clear();
            user.addAuthority(authorityService.getUser());
            if (managedUserDTO.isAdmin()) {
                user.addAuthority(authorityService.getAdmin());
            }
            // if password field is not blank, then update password
            if (!(managedUserDTO.getPassword() == null || "".equals(managedUserDTO.getPassword()))) {
                user.setPassword(managedUserDTO.getPassword());
                userService.updatePassword(user);
            }
            userService.update(user);
            return "redirect:/admin/index";
        }
    }
}
