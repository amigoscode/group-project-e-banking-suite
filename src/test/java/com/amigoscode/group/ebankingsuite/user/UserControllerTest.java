package com.amigoscode.group.ebankingsuite.user;

import com.amigoscode.group.ebankingsuite.account.AccountService;
import com.amigoscode.group.ebankingsuite.user.requests.ChangePasswordRequest;
import com.amigoscode.group.ebankingsuite.user.requests.UserAuthenticationRequests;
import com.amigoscode.group.ebankingsuite.user.requests.UserRegistrationRequest;
import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private JWTService jwtService;
    @Mock
    private AccountService accountService;

    private UserController userController;


    @BeforeEach
    public void setUp() {
        this.userService = new UserService(userRepository, jwtService, accountService);
        this.userController = new UserController(userService, jwtService);
    }

    @Test
    void willReturn202WhenSavingUser(){
        //given
        UserRegistrationRequest userRequest = new UserRegistrationRequest(
                "lawal",
                "larwal@mail.com",
                "12345"
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        given(userRepository.existsByEmailAddress(userRequest.emailAddress())).willReturn(false);

        //when
        ResponseEntity<ApiResponse> responseEntity = userController.saveUser(userRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void willReturn409WhenEmailExistsWhileSavingNewUser(){
        //given
        UserRegistrationRequest userRequest = new UserRegistrationRequest(
                "lawal",
                "larwal@mail.com",
                "12345"
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        given(userRepository.existsByEmailAddress(userRequest.emailAddress())).willReturn(true);
        ResponseEntity<ApiResponse> responseEntity = userController.saveUser(userRequest);

        //when
        //then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(409);
        assertThat(responseEntity.getBody()).isInstanceOf(ApiResponse.class);
    }

    @Test
    void willReturn201WhenUsernameAndPasswordMatchForAuthentication(){
        //given
        UserAuthenticationRequests authenticationRequests = new UserAuthenticationRequests(
                "larwal@mail.com",
                "12345"
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        User mockUser = new User(
                1,
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("12345"),
                true);

        given(userRepository.findByEmailAddress(authenticationRequests.emailAddress())).willReturn(Optional.of(mockUser));


        //when
        ResponseEntity<ApiResponse> responseEntity = userController.authenticateUser(authenticationRequests);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void willReturn409WhenUsernameAndPasswordDoesNotMatchForAuthentication(){
        //given
        UserAuthenticationRequests authenticationRequests = new UserAuthenticationRequests(
                "larwal@mail.com",
                "123"
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        User mockUser = new User(
                1,
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("12345"),
                true
        );
        given(userRepository.findByEmailAddress(authenticationRequests.emailAddress())).
                willReturn(Optional.of(mockUser));

        //when
        ResponseEntity<ApiResponse> responseEntity = userController.authenticateUser(authenticationRequests);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    @Test
    void willReturn404WhenUsernameDoesNotMatchAnyUserForAuthentication(){
        //given
        UserAuthenticationRequests authenticationRequests = new UserAuthenticationRequests(
                "larwal@mail.com",
                "123"
        );

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        given(userRepository.findByEmailAddress(authenticationRequests.emailAddress())).
                willReturn(Optional.empty());

        //when
        ResponseEntity<ApiResponse> responseEntity = userController.authenticateUser(authenticationRequests);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void willReturn200WhenPasswordMatchForChangePassword(){
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
                "12345",
                "1234"
        );

        Integer userId = 1;
        User mockUser = new User(
                1,
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("12345"),
                true
        );
        String jwt = "Bearer "+ "testToken";

        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(jwtService.extractUserIdFromToken(jwt)).willReturn(1);


        //when
        ResponseEntity<ApiResponse> responseEntity =
                userController.changeUserPassword(jwt,changePasswordRequest);
        //then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void willReturn406WhenOldPasswordDoesNotMatchForChangePassword(){
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
                "1234",
                "12345"
        );

        Integer userId = 1;
        User mockUser = new User(
                1,
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("12345"),
                true
        );
        String jwt = "Bearer "+ "testToken";

        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));
        given(jwtService.extractUserIdFromToken(jwt)).willReturn(1);


        //when
        ResponseEntity<ApiResponse> responseEntity =
                userController.changeUserPassword(jwt,changePasswordRequest);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    void willReturn404WhenNoUserFoundForUserIdForChangePassword(){
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
                "12345",
                "12345"
        );

        Integer userId = 1;
        String jwt = "Bearer "+ "testToken";

        given(jwtService.extractUserIdFromToken(jwt)).willReturn(1);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when
        ResponseEntity<ApiResponse> responseEntity =
                userController.changeUserPassword(jwt,changePasswordRequest);
        //then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }



}