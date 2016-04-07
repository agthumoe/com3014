package com.surrey.com3014.group5.controllers.api;

import com.surrey.com3014.group5.dto.LeaderboardDTO;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring MVC controller to handle lobbies.
 *
 * @author Spyros Balkonis
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api")
@Api(value = "Leaderboard", description = "Operation about leaderboard", consumes = "application/json")
public class LeaderboardController {
//    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyController.class);

    @Autowired
    private LeaderboardService leaderboardService;

    @RequestMapping(method = RequestMethod.GET, value = "/leaderboard")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getLeaderboard(){
        List<Leaderboard> leaderboards = getLeaderboardService().findAllByOrderByRatioDesc();
        List<LeaderboardDTO> leaderboardDTOs = leaderboards.stream().map(leaderboard -> new LeaderboardDTO(leaderboard.getUser().getUsername(), leaderboard.getWins(), leaderboard.getLosses(), leaderboard.getRatio())).collect(Collectors.toList());
        return ResponseEntity.ok(leaderboardDTOs);
    }

    public LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }

}
