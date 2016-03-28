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

    @RequestMapping("/help")
    public String help() {
        return "help";
    }

    @RequestMapping("/game")
    public String game() {
        return "game";
    }

    @RequestMapping(value = "/lobby")
    public String getLobbyPage() {
        return "lobby";
    }
}
