package com.surrey.com3014.group5.controllers.api;

import com.surrey.com3014.group5.dto.StatusDTO;
import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(value = "Lobby", description = "Operation about lobby", consumes = "application/json")
public class LobbyController {

    @Autowired
    private SessionRegistry sessionRegistry;

    private LeaderboardService leaderboardService;

    @ApiOperation(value = "Get all online users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = List.class, message = "success"),
        @ApiResponse(code=401, message = "Unauthorized", response = ErrorDTO.class)
    })
    @RequestMapping(method = RequestMethod.GET, value = "/users")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> listLoggedInUsers() {
        return ResponseEntity.ok(sessionRegistry.getAllPrincipals());
    }

    @ApiOperation(value = "Get the provided user online status")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = StatusDTO.class, message = "success"),
        @ApiResponse(code=401, message = "Unauthorized", response = ErrorDTO.class)
    })

    @RequestMapping(method = RequestMethod.GET, value = "/users/by_username/{username}")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> checkUserIsOnline(@PathVariable("username") String username) {
        List<Object> onlineUsers = sessionRegistry.getAllPrincipals();

        for(Object onlineUser : onlineUsers){
            if(onlineUser.equals(username)){
                return ResponseEntity.ok(new StatusDTO(username, StatusDTO.ONLINE));
            }
        }
        return ResponseEntity.ok(new StatusDTO(username, StatusDTO.OFFLINE));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/leaderboard")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getLeaderboard(){
        List<Leaderboard> leaderboards = getLeaderboardService().findAllByOrderByRatioDesc();

        return ResponseEntity.ok(leaderboards);
    }

    public LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }

    @Autowired
    public void setLeaderboardService(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }
}
