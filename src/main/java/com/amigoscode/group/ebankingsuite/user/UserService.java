package com.amigoscode.group.ebankingsuite.user;

import com.amigoscode.group.ebankingsuite.account.Account;
import com.amigoscode.group.ebankingsuite.account.AccountService;
import com.amigoscode.group.ebankingsuite.exception.ResourceExistsException;
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
            throw new ResourceExistsException("email address is taken");
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
        User existingUser = getUserByEmailAddress(requests.emailAddress());

        if (passwordMatches(requests.password(),existingUser.getPassword())){
            Map<String,Object> claims = Map.of("userId", existingUser.getId());
            return jwtService.generateToken(claims,existingUser);
        }
        throw new InvalidAuthenticationException("Invalid username or password");

    }

    public void changeUserPassword(ChangePasswordRequest request, Integer userId){
        User existingUser = getUserByUserId(userId);

        if(!bCryptPasswordEncoder.matches(request.oldPassword(),existingUser.getPassword())){
            throw new ValueMismatchException("old password does not match");
        }

        existingUser.setPassword(bCryptPasswordEncoder.encode(request.newPassword()));
        updateUser(existingUser);


    }

    public User getUserByUserId(int userId){
        Optional<User> existingUser = userRepository.findById(userId);
        if(existingUser.isEmpty()){
            throw new ResourceNotFoundException("user not found");
        }
        return existingUser.get();
    }
    public User getUserByEmailAddress(String emailAddress){
        Optional<User> existingUser = userRepository.findByEmailAddress(emailAddress);
        if(existingUser.isEmpty()){
            throw new ResourceNotFoundException("user not found");
        }
        return existingUser.get();
    }


    public String encodePassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

}
