package com.surrey.com3014.group5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "/lobby")
    public String getLobbyPage() {
        return "lobby";
    }

    @RequestMapping("/game/{gameID}")
    public String game(@PathVariable("gameID") String gameID, Model model) {
        model.addAttribute("gameID", gameID);
        return "game";
    }
}
