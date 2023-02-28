package com.amogoscode.groupe.ebankingsuite.User;

import com.amogoscode.groupe.ebankingsuite.User.resquests.UserAuthenticationRequests;
import com.amogoscode.groupe.ebankingsuite.User.resquests.UserRegistrationRequest;
import com.amogoscode.groupe.ebankingsuite.config.JWTService;
import com.amogoscode.groupe.ebankingsuite.universal.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserController userController;

    @BeforeEach
    public void setUp() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.userService = new UserService(userRepository, jwtService);
        this.userController = new UserController(userService);
    }

    @Test
    void willReturn200WhenSavingUser(){
        //given
        UserRegistrationRequest userRequest = new UserRegistrationRequest(
                "lawal",
                "larwal@mail.com",
                "12345"
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        given(userRepository.existsByEmailAddress(userRequest.emailAddress())).willReturn(false);


        ResponseEntity<ApiResponse> responseEntity = userController.saveUser(userRequest);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void willReturn409WhenSavingUserWhenEmailExists(){
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
    void willReturn200AndJWTWhenUsernameAndPasswordMatchForAuthentication(){
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


        ResponseEntity<ApiResponse> responseEntity = userController.authenticateUser(authenticationRequests);
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }



}