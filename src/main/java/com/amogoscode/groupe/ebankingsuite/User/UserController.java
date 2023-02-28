package com.amogoscode.groupe.ebankingsuite.User;

import com.amogoscode.groupe.ebankingsuite.User.resquests.UserAuthenticationRequests;
import com.amogoscode.groupe.ebankingsuite.User.resquests.UserRegistrationRequest;
import com.amogoscode.groupe.ebankingsuite.exception.InvalidAuthenticationException;
import com.amogoscode.groupe.ebankingsuite.exception.ResourceNotFoundException;
import com.amogoscode.groupe.ebankingsuite.universal.ApiResponse;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody UserRegistrationRequest request){
        try {
            userService.createNewUser(request);
            return  new ResponseEntity<>(
                    new ApiResponse("user created successfully"), HttpStatus.OK);
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
