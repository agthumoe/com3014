package com.surrey.com3014.group5.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring MVC controller to handle lobbies.
 *
 * @author Spyros Balkonis
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyController.class);

    @Autowired
    private SessionRegistry sessionRegistry;

    @RequestMapping(method = RequestMethod.GET, value = "/online")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> listLoggedInUsers() {
        return ResponseEntity.ok(sessionRegistry.getAllPrincipals());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> checkUserIsOnline(@RequestParam(value = "username", required = true) String username) {
        List<Object> onlineUsers = sessionRegistry.getAllPrincipals();

        for(Object onlineUser : onlineUsers){
            if(onlineUser.equals(username)){
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.ok(false);
    }


}
