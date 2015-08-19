package uk.co.landbit.twittertofeed.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.co.landbit.twittertofeed.user.controller.SignupForm;
import uk.co.landbit.twittertofeed.user.domain.User;
import uk.co.landbit.twittertofeed.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

  
    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository accountRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
    }
    
   

//    @Override
//    public User signup(SignupForm userform) {
//	LOG.debug("Saving userto DB, email {}", userform.getEmail());
//	User user = new User();
//	user.setEmail(userform.getEmail());
//	user.setFirstName(userform.getFirstName());
//	user.setLastName(userform.getLastName());
//	user.setRole(userform.getRole());
//	user.setPassword(new BCryptPasswordEncoder().encode(userform.getPassword()));
//	user.setRole(userform.getRole());
//	return userRepository.save(user);
//    }
    
    
    @Transactional
    @Override
    public User signup(SignupForm userform) { //throws DuplicateEmailException {
        LOG.debug("Registering new user account with information: {}", userform);

        if (emailExist(userform.getEmail())) {
            LOG.debug("Email: {} exists. Throwing exception.", userform.getEmail());
            //throw new DuplicateEmailException("The email address: " + userAccountData.getEmail() + " is already in use.");
        }

        LOG.debug("Email: {} does not exist. Continuing registration.", userform.getEmail());

        String encodedPassword = encodePassword(userform);

        User.Builder user = User.getBuilder()
                .email(userform.getEmail())
                .firstName(userform.getFirstName())
                .lastName(userform.getLastName())
                .password(encodedPassword);

        if (userform.isSocialSignIn()) {
            user.signInProvider(userform.getSignInProvider());
        }

        User registered = user.build();

        LOG.debug("Persisting new user with information: {}", registered);

        return accountRepository.save(registered);
    }

    private boolean emailExist(String email) {
        LOG.debug("Checking if email {} is already found from the database.", email);

//        User user = accountRepository.findByEmail(email);
//
//        if (user != null) {
//            LOG.debug("User account: {} found with email: {}. Returning true.", user, email);
//            return true;
//        }

        LOG.debug("No user account found with email: {}. Returning false.", email);

        return false;
    }

    private String encodePassword(SignupForm form) {
        String encodedPassword = null;

        if (!form.isSocialSignIn()) {
            LOG.debug("Registration is normal registration. Encoding password.");
            encodedPassword = passwordEncoder.encode(form.getPassword());
        }

        return encodedPassword;
    }
    
    

}
