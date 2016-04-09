package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.exceptions.BadRequestException;
import com.surrey.com3014.group5.exceptions.ResourceNotFoundException;
import com.surrey.com3014.group5.exceptions.UnauthorizedException;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.AbstractMutableService;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Spyros Balkonis
 * @author Aung Thu Moe
 */
@Service("userService")
public class UserServiceImpl extends AbstractMutableService<User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        super(userRepository);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return getUserRepository().findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return getUserRepository().findByEmail(email);
    }

    public UserRepository getUserRepository() {
        return (UserRepository) super.getRepository();
    }

    @Override
    public <S extends User> S create(S s) {
        s.setPassword(passwordEncoder.encode(s.getPassword())); //Password hashed and salt added.
        Leaderboard leaderboard = new Leaderboard(s);
        s.setLeaderboard(leaderboard);
        s = super.create(s);
        leaderboardService.create(leaderboard);
        return s;
    }

    @Override
    public boolean validate(long id, String password) {
        Optional<User> maybeUser = findOne(id);
        if (maybeUser.isPresent()) {
            return passwordEncoder.matches(password, maybeUser.get().getPassword());
        }
        return false;
    }

    @Override
    @Transactional
    public UsernamePasswordAuthenticationToken authenticate(final String username, final String password) {
        Optional<User> maybeUser = this.findByUsername(username);
        if (!maybeUser.isPresent()) {
            throw new ResourceNotFoundException();
        }
        User user = maybeUser.get();
        if (!user.isEnabled()) {
            throw new UnauthorizedException();
        }
        if (this.passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        } else {
            throw new BadRequestException();
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getCurrentLogin() {
        return getUserRepository().findByUsername(SecurityUtils.getCurrentUsername());
    }

    @Override
    public List<User> getAll() {
        return getUserRepository().findAll();
    }

    @Override
    public void updatePassword(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    }

    @Override
    public boolean validate(String password) {
        Optional<User> maybeUser = findByUsername(SecurityUtils.getCurrentUsername());
        if (maybeUser.isPresent()) {
            return passwordEncoder.matches(password, maybeUser.get().getPassword());
        }
        return false;
    }

    @Override
    public Page<User> getUsers(Pageable pageRequest) {
        return getUserRepository().findAll(pageRequest);
    }
}
