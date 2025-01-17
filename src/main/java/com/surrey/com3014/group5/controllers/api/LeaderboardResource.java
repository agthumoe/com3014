package com.surrey.com3014.group5.controllers.api;

import com.surrey.com3014.group5.dto.leaderboards.LeaderboardDTO;
import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Rest controller to handle lobbies.
 *
 * @author Spyros Balkonis
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api/leaderboard")
@Api(value = "Leaderboard", description = "Operation about leaderboard", consumes = "application/json")
public class LeaderboardResource {

    /**
     * LeaderboardService to access leaderboard dao.
     */
    @Autowired
    private LeaderboardService leaderboardService;

    /**
     * Get top 10 leaderboard scores.
     *
     * @return top 10 leaderboard scores.
     */
    @ApiOperation(value = "Get top 10 leaderboard scores", notes = "This can only be done by logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = LeaderboardDTO.class, responseContainer = "List")
    })
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getLeaderboard() {
        List<Leaderboard> leaderboards = getLeaderboardService().findAllByOrderByRatingDescUserAsc();
        List<LeaderboardDTO> leaderboardDTOs = leaderboards.stream().map(leaderboard -> new LeaderboardDTO(new UserDTO(leaderboard.getUser()), leaderboard.getWins(), leaderboard.getLosses(), leaderboard.getRating())).collect(Collectors.toList());
        return ResponseEntity.ok(leaderboardDTOs);
    }

    public LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }

}
