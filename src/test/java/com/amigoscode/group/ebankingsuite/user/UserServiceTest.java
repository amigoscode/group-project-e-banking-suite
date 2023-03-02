package com.amigoscode.group.ebankingsuite.user;

import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.user.resquests.UserAuthenticationRequests;
import com.amigoscode.group.ebankingsuite.user.resquests.UserRegistrationRequest;
import com.amigoscode.group.ebankingsuite.exception.InvalidAuthenticationException;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Java6Assertions.assertThat;



@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTService jwtService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        this.userService = new UserService(userRepository, jwtService);
    }

    @Test
    void canSaveUserWhenEmailAddressIsUnique(){
        //given
        UserRegistrationRequest testRequest = new UserRegistrationRequest(
                "pen tami",
                "pentami@mailer.com",
                "12345"
        );
        given(userRepository.existsByEmailAddress(testRequest.emailAddress())).willReturn(false);

        //when
        userService.createNewUser(testRequest);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue()).isEqualToComparingOnlyGivenFields(new User(
                testRequest.fullName(),testRequest.emailAddress(),testRequest.password(),true),
                "fullName","emailAddress","isNotBlocked");
    }

    @Test
    void willThrowIllegalArgumentExceptionWhenEmailAddressIsNotUniqueWhileSavingUser(){
        //given
        UserRegistrationRequest testRequest = new UserRegistrationRequest(
                "pen tami",
                "pentami@mailer.com",
                "12345"
        );
        given(userRepository.existsByEmailAddress(testRequest.emailAddress())).willReturn(true);

        //when
        //that
        System.out.println("here");
        assertThatThrownBy(()-> userService.createNewUser(testRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email address is taken");
    }

    @Test
    void canAuthenticateUserEmailAndPasswordMatch(){
        //given
        UserAuthenticationRequests authenticationRequests = new UserAuthenticationRequests(
                "larwal@mail.com",
                "12345"
        );
        User mockUser = new User(
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("12345"),
                true);
        given(userRepository.findByEmailAddress(
                authenticationRequests.emailAddress())).willReturn(Optional.of(mockUser));

        //when
        userService.authenticateUser(authenticationRequests);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(jwtService).generateToken(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue()).isEqualToComparingOnlyGivenFields(new User(
                        mockUser.getFullName(),mockUser.getEmailAddress(),mockUser.getPassword(),true),
                "fullName","emailAddress","isNotBlocked");
    }
    @Test
    void willThrowInvalidAuthenticationExceptionWhenEmailAndPasswordDoesNotMatchWhenAuthenticatingUser(){
        //given
        UserAuthenticationRequests authenticationRequests = new UserAuthenticationRequests(
                "larwal@mail.com",
                "12345"
        );
        User mockUser = new User(
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("1234"),
                true);

        given(userRepository.findByEmailAddress(
                authenticationRequests.emailAddress())).willReturn(Optional.of(mockUser));

        //when
        //that
        assertThatThrownBy(()-> userService.authenticateUser(authenticationRequests))
                .isInstanceOf(InvalidAuthenticationException.class)
                .hasMessage("Invalid username or password");
    }

    @Test
    void willThrowResourceNotFoundExceptionWhenEmailDoesNotExistsWhileAuthenticatingUser(){
        //given
        UserAuthenticationRequests authenticationRequests = new UserAuthenticationRequests(
                "larwal@mail.com",
                "12345"
        );

        given(userRepository.findByEmailAddress(
                authenticationRequests.emailAddress())).willReturn(Optional.empty());

        //when
        //that
        assertThatThrownBy(()-> userService.authenticateUser(authenticationRequests))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user does not exist");
    }

}