package com.surrey.com3014.group5.services.user;

import com.surrey.com3014.group5.dto.UserDTO;
import com.surrey.com3014.group5.exceptions.NotFoundException;
import com.surrey.com3014.group5.models.impl.Authority;
import com.surrey.com3014.group5.models.impl.User;
import com.surrey.com3014.group5.repositories.UserRepository;
import com.surrey.com3014.group5.security.SecurityUtils;
import com.surrey.com3014.group5.services.AbstractMutableService;
import com.surrey.com3014.group5.services.authority.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.surrey.com3014.group5.configs.SecurityConfig.*;

/**
 * @author Spyros Balkonis
 */
@Service("userService")
public class UserServiceImpl extends AbstractMutableService<User> implements UserService {

//    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        super(userRepository);
    }


    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = getUserRepository().findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "User with username: " + username + " does not exist.");
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> userOptional = getUserRepository().findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new NotFoundException(HttpStatus.NOT_FOUND, "User with email: " + email + " does not exist.");
    }

    public UserRepository getUserRepository() {
        return (UserRepository) super.getRepository();
    }

    @Override
    public <S extends User> S create(S s) {
    	if (!(null == s.getName() || s.getName().trim().equals(""))) {
    		s.setPassword(passwordEncoder.encode(s.getPassword())); //Password hashed and salt added.
    	}
        return super.create(s);
    }

    @Override
    public boolean validate(User user) {
        User userFromDatabase = findByEmail(user.getEmail());
        return passwordEncoder.matches(user.getPassword(), userFromDatabase.getPassword());
    }

    @Override
    @Transactional
    public UsernamePasswordAuthenticationToken authenticate(final String username, final String password) {
        User user = this.findByUsername(username);
        if (this.passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("User authentication failed");
        }
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        Optional<User> userOptional = getUserRepository().findByUsername(SecurityUtils.getCurrentLogin());
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new NotFoundException(HttpStatus.UNAUTHORIZED, "No user has been authenticated yet");
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
}
