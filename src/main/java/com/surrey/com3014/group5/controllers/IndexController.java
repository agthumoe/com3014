package com.surrey.com3014.group5.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by aungthumoe on 21/02/2016.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

}
