package com.surrey.com3014.group5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring MVC controller to handle general pages.
 *
 * @author Aung Thu Moe
 */
@Controller
public class MainController {

    /**
     * Get index page.
     *
     * @return index page.
     */
    @RequestMapping({"/", "index"})
    public String index() {
        return "index";
    }

    /**
     * Get help page.
     *
     * @return help page.
     */
    @RequestMapping("/help")
    public String help() {
        return "help";
    }

    /**
     * Get game lobby page.
     *
     * @return game lobby page.
     */
    @RequestMapping(value = "/lobby")
    public String getLobbyPage() {
        return "lobby";
    }

    /**
     * Get game page.
     *
     * @param gameID new game id generated dynamically.
     * @return Game page to play the game.
     */
    @RequestMapping("/game/{gameID}")
    public String game(@PathVariable("gameID") String gameID, Model model) {
        model.addAttribute("gameID", gameID);
        return "game";
    }
}
