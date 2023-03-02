package com.amogoscode.group.ebankingsuite.user;

import com.amogoscode.group.ebankingsuite.user.resquests.UserAuthenticationRequests;
import com.amogoscode.group.ebankingsuite.user.resquests.UserRegistrationRequest;
import com.amogoscode.group.ebankingsuite.config.JWTService;
import com.amogoscode.group.ebankingsuite.universal.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    private UserController userController;

    @BeforeEach
    public void setUp() {
        this.userService = new UserService(userRepository, jwtService);
        this.userController = new UserController(userService);
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
        //the
        ResponseEntity<ApiResponse> responseEntity = userController.saveUser(userRequest);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
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
    void willReturn200WhenUsernameAndPasswordMatchForAuthentication(){
        //given
        UserAuthenticationRequests authenticationRequests = new UserAuthenticationRequests(
                "larwal@mail.com",
                "12345"
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        User mockUser = new User(
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("12345"),
                true);

        given(userRepository.findByEmailAddress(authenticationRequests.emailAddress())).willReturn(Optional.of(mockUser));


        //when
        //then
        ResponseEntity<ApiResponse> responseEntity = userController.authenticateUser(authenticationRequests);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }



}