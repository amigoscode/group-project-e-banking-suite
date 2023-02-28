package com.amogoscode.groupe.ebankingsuite.User;

import com.amogoscode.groupe.ebankingsuite.User.resquests.UserAuthenticationRequests;
import com.amogoscode.groupe.ebankingsuite.User.resquests.UserRegistrationRequest;
import com.amogoscode.groupe.ebankingsuite.config.JWTService;
import com.amogoscode.groupe.ebankingsuite.exception.InvalidAuthenticationException;
import com.amogoscode.groupe.ebankingsuite.exception.ResourceNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private static final BCryptPasswordEncoder bCryptPasswordEncoder =
            new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, JWTService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    //this service creates a new user provided the email does not exist
    //later this method will be updated to invoke the create user account service to create an account for the user
    public void createNewUser(UserRegistrationRequest userRegistrationRequest){
        if(!userRepository.existsByEmailAddress(userRegistrationRequest.emailAddress())){
            userRepository.save(new User(
                    userRegistrationRequest.fullName(),
                    userRegistrationRequest.emailAddress(),
                    bCryptPasswordEncoder.encode(userRegistrationRequest.password()),
                    true
            ));
            return;
        }
        throw new IllegalArgumentException("email address is taken");
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword,encodedPassword);
    }


    public String authenticateUser(UserAuthenticationRequests requests){
        Optional<User> existingUser = userRepository.findByEmailAddress(requests.emailAddress());

        if(existingUser.isPresent()){
            if (passwordMatches(requests.password(),existingUser.get().getPassword())){
                return jwtService.generateToken(existingUser.get());
            }
            throw new InvalidAuthenticationException("Invalid username or password");
        }
        throw new ResourceNotFoundException("user does not exist");
    }

    public String encodePassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

}
