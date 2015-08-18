package uk.co.landbit.twittertofeed.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import uk.co.landbit.twittertofeed.user.controller.SignupForm;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
	this.userRepository = userRepository;
    }

    @Override
    public User signup(SignupForm userform) {
	LOG.debug("Saving userto DB, email {}", userform.getEmail());
	User user = new User();
	user.setEmail(userform.getEmail());
	user.setFirstName(userform.getFirstName());
	user.setLastName(userform.getLastName());
	user.setRole(userform.getRole());
	user.setPassword(new BCryptPasswordEncoder().encode(userform.getPassword()));
	user.setRole(userform.getRole());
	return userRepository.save(user);
    }

}
