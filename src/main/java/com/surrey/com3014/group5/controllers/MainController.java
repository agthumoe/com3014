package com.surrey.com3014.group5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Aung Thu Moe
 */
@Controller
public class MainController {

    @RequestMapping({"/", "index"})
    public String index(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Tron Game | User Registration");
        model.addAttribute("description", "This is user registration page for Tron game");
        return "register";
    }

    @RequestMapping(value = "/admin**", method = RequestMethod.GET)
    public ModelAndView adminPage() {

        ModelAndView model = new ModelAndView();
        model.addObject("title", "Spring Security Hello World");
        model.addObject("message", "This is protected page - Admin Page!");
        model.setViewName("admin");

        return model;

    }

    @RequestMapping(value = "/login")
    public String loginPage(
        @RequestParam(value = "errors", required = false) String error,
        @RequestParam(value = "logout", required = false) String logout,
        Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addAttribute("msg", "You've been logged out successfully.");
        }
        return "login";
    }

}
