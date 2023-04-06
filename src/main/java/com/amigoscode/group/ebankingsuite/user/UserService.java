package com.amigoscode.group.ebankingsuite.user;

import com.amigoscode.group.ebankingsuite.account.Account;
import com.amigoscode.group.ebankingsuite.account.AccountService;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.user.requests.ChangePasswordRequest;
import com.amigoscode.group.ebankingsuite.user.requests.UserAuthenticationRequests;
import com.amigoscode.group.ebankingsuite.user.requests.UserRegistrationRequest;
import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.exception.InvalidAuthenticationException;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final AccountService accountService;
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository, JWTService jwtService, AccountService accountService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.accountService = accountService;
    }


    public void updateUser(User existingUser){
        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);
    }


    /**
     * this service creates a new user provided the email does not exist and also invokes the account
     * service to create a new account for the user
     */
    @Transactional
    public void createNewUser(UserRegistrationRequest userRegistrationRequest){
        if(userRepository.existsByEmailAddress(userRegistrationRequest.emailAddress())){
            throw new IllegalArgumentException("email address is taken");
        }
        User newUser = new User(
                userRegistrationRequest.fullName(),
                userRegistrationRequest.emailAddress(),
                bCryptPasswordEncoder.encode(userRegistrationRequest.password()),
                true);

        userRepository.save(newUser);
        accountService.createAccount(new Account(newUser.getId()));
    }

    public boolean passwordMatches(String rawPassword, String encodedPassword){
        return bCryptPasswordEncoder.matches(rawPassword,encodedPassword);
    }


    public String authenticateUser(UserAuthenticationRequests requests){
        Optional<User> existingUser = userRepository.findByEmailAddress(requests.emailAddress());

        if(existingUser.isPresent()){
            if (passwordMatches(requests.password(),existingUser.get().getPassword())){
                Map<String,Object> claims = Map.of("userId", existingUser.get().getId());
                return jwtService.generateToken(claims,existingUser.get());
            }
            throw new InvalidAuthenticationException("Invalid username or password");
        }
        throw new ResourceNotFoundException("user does not exist");
    }

    public void changeUserPassword(ChangePasswordRequest request, Integer userId){
        Optional<User> existingUser = userRepository.findById(String.valueOf(userId));
        if(existingUser.isPresent()){
           if(bCryptPasswordEncoder.matches(request.oldPassword(),existingUser.get().getPassword())){
               existingUser.get().setPassword(bCryptPasswordEncoder.encode(request.newPassword()));
               updateUser(existingUser.get());
               return;
           }
           throw new ValueMismatchException("old password does not match");
        }
        throw new ResourceNotFoundException("user not found");
    }


    public String encodePassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

}
