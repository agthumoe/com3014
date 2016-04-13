package com.surrey.com3014.group5.controllers.api;

import com.surrey.com3014.group5.dto.LeaderboardDTO;
import com.surrey.com3014.group5.dto.errors.ErrorDTO;
import com.surrey.com3014.group5.dto.users.UserDTO;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import com.surrey.com3014.group5.services.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring MVC controller to handle lobbies.
 *
 * @author Spyros Balkonis
 * @author Aung Thu Moe
 */
@RestController
@RequestMapping("/api/leaderboard")
@Api(value = "Leaderboard", description = "Operation about leaderboard", consumes = "application/json")
public class LeaderboardResource {
//    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyController.class);

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "Get top 10 leaderboard scores", notes = "This can only be done by logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = LeaderboardDTO.class, responseContainer = "List")
    })
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getLeaderboard(){
        List<Leaderboard> leaderboards = getLeaderboardService().findAllByOrderByRatioDescUserAsc();
        List<LeaderboardDTO> leaderboardDTOs = leaderboards.stream().map(leaderboard -> new LeaderboardDTO(new UserDTO(leaderboard.getUser()), leaderboard.getWins(), leaderboard.getLosses(), leaderboard.getRatio())).collect(Collectors.toList());
        return ResponseEntity.ok(leaderboardDTOs);
    }

//    public ResponseEntity<?> win(long userID) {
//        Optional<User> maybeUser = userService.findOne(userID);
//        if (maybeUser.isPresent()) {
//            Optional<Leaderboard> maybeLeaderboard = leaderboardService.findByUser(maybeUser.get());
//            if (maybeLeaderboard.isPresent()) {
//                final Leaderboard leaderboard = maybeLeaderboard.get();
//                leaderboard.setWins(leaderboard.getWins() + 1);
//                return ResponseEntity.noContent().build();
//            }
//        }
//        return new ResponseEntity<>(new ErrorDTO(HttpStatus.NOT_FOUND, "The requested resource does not exist"), HttpStatus.NOT_FOUND);
//    }


    public LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }

}
