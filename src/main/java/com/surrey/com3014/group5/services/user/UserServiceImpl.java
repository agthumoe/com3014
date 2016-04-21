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
 * Implementation of UserService.
 *
 * @author Spyros Balkonis
 * @author Aung Thu Moe
 */
@Service("userService")
public class UserServiceImpl extends AbstractMutableService<User> implements UserService {

    /**
     * To encode password.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * To access leaderboard information.
     */
    @Autowired
    private LeaderboardService leaderboardService;

    /**
     * Create a new UserService with the provided userRepository.
     *
     * @param userRepository to access user information.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return getUserRepository().findByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return getUserRepository().findByEmail(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findByUsernameContaining(String username) {
        return getUserRepository().findByUsernameContaining(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findByEmailContaining(String email) {
        return getUserRepository().findByEmailContaining(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findByNameContaining(String name) {
        return getUserRepository().findByNameContaining(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findByEnabled(boolean enabled) {
        return getUserRepository().findByEnabled(enabled);
    }

    private UserRepository getUserRepository() {
        return (UserRepository) super.getRepository();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <S extends User> S create(S s) {
        s.setPassword(passwordEncoder.encode(s.getPassword())); //Password hashed and salt added.
        Leaderboard leaderboard = new Leaderboard(s);
        s.setLeaderboard(leaderboard);
        s = super.create(s);
        leaderboardService.create(leaderboard);
        return s;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getCurrentLogin() {
        return getUserRepository().findByUsername(SecurityUtils.getCurrentUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAll() {
        return getUserRepository().findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(String password) {
        Optional<User> maybeUser = findByUsername(SecurityUtils.getCurrentUsername());
        if (maybeUser.isPresent()) {
            return passwordEncoder.matches(password, maybeUser.get().getPassword());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<User> getPagedList(Pageable pageRequest) {
        return getUserRepository().findAll(pageRequest);
    }
}
