package com.amogoscode.group.ebankingsuite.user;

import com.amogoscode.group.ebankingsuite.user.resquests.UserAuthenticationRequests;
import com.amogoscode.group.ebankingsuite.user.resquests.UserRegistrationRequest;
import com.amogoscode.group.ebankingsuite.exception.InvalidAuthenticationException;
import com.amogoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import com.amogoscode.group.ebankingsuite.universal.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody UserRegistrationRequest request){
        try {
            userService.createNewUser(request);
            return  new ResponseEntity<>(
                    new ApiResponse("user created successfully"), HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return  new ResponseEntity<>(
                    new ApiResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(
            @RequestBody UserAuthenticationRequests userAuthenticationRequests){
       try {
           String jwt = userService.authenticateUser(userAuthenticationRequests);
           return new ResponseEntity<>(
                   new ApiResponse("user logged in successfully",jwt),HttpStatus.OK);
       }catch (InvalidAuthenticationException e){
           return  new ResponseEntity<>(
                   new ApiResponse(e.getMessage()), HttpStatus.FORBIDDEN);
       }catch (ResourceNotFoundException e){
           return  new ResponseEntity<>(
                   new ApiResponse(e.getMessage()), HttpStatus.NOT_FOUND);
       }

    }

}
