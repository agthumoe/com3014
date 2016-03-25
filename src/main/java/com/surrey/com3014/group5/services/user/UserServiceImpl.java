package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.configs.SecurityConfig;
import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.Leaderboard;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.AbstractMutableService;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import com.surrey.com3014.group5.services.leaderboard.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.surrey.com3014.group5.configs.SecurityConfig.*;

/**
 * @author Spyros Balkonis
 * @author Aung Thu Moe
 */
@Service("userService")
public class UserServiceImpl extends AbstractMutableService<User> implements UserService {

//    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityService authorityService;

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
    	if (!(null == s.getName() || s.getName().trim().equals(""))) {
    		s.setPassword(passwordEncoder.encode(s.getPassword())); //Password hashed and salt added.
    	}
        Leaderboard leaderboard = new Leaderboard(s);
        s.setLeaderboard(leaderboard);
        s = super.create(s);
        leaderboardService.create(leaderboard);
        return s;
    }

    @Override
    public boolean validate(User user) {
        Optional<User> maybeUser = findByEmail(user.getEmail());
        if (maybeUser.isPresent()) {
            return passwordEncoder.matches(user.getPassword(), maybeUser.get().getPassword());
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "The requested user with username: " + user.getUsername() + ", does not exist!");
    }

    @Override
    @Transactional
    public UsernamePasswordAuthenticationToken authenticate(final String username, final String password) {
        Optional<User> maybeUser = this.findByUsername(username);
        if (!maybeUser.isPresent()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "The requested user with username: " + username + ", does not exist!");
        }
        User user = maybeUser.get();
        if (this.passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("User authentication failed");
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getCurrentLogin() {
        return getUserRepository().findByUsername(SecurityUtils.getCurrentLogin());
    }

    @Override
    public User createUserWithAuthorities(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getName());
        for (String authority: userDTO.getAuthorities()) {
            Optional<Authority> authorityOptional = authorityService.findByType(authority);
            if (authorityOptional.isPresent()) {
                user.addAuthority(authorityOptional.get());
            } else {
                throw new NotFoundException("Authority provided does not exist in the system");
            }
        }
        // at least every user will have USER role by default
        if (user.getAuthorities().size() == 0) {
            user.addAuthority(authorityService.findByType(SecurityConfig.USER).get());
        }
        return create(user);
    }

    @Override
    public User create(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(), userDTO.getName());
        Optional<Authority> authorityOptional = authorityService.findByType(USER);
        if (authorityOptional.isPresent()) {
            user.addAuthority(authorityOptional.get());
            return this.create(user);
        }
        throw new NotFoundException("default authority: " + USER + " ,does not exist in the system!");
    }

    @Override
    public List<User> getAll() {
        return getUserRepository().findAll();
    }
}
