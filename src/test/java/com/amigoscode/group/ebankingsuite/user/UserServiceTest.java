package com.amigoscode.group.ebankingsuite.user;

import com.amigoscode.group.ebankingsuite.account.AccountService;
import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.exception.ResourceExistsException;
import com.amigoscode.group.ebankingsuite.exception.ValueMismatchException;
import com.amigoscode.group.ebankingsuite.user.requests.ChangePasswordRequest;
import com.amigoscode.group.ebankingsuite.user.requests.UserAuthenticationRequests;
import com.amigoscode.group.ebankingsuite.user.requests.UserRegistrationRequest;
import com.amigoscode.group.ebankingsuite.exception.InvalidAuthenticationException;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Java6Assertions.assertThat;



@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JWTService jwtService;
    @Mock
    private AccountService accountService;


    @BeforeEach
    public void setUp() {
        this.userService = new UserService(userRepository, jwtService, accountService);
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
    void willThrowResourceExistsExceptionWhenEmailAddressIsNotUniqueWhileSavingUser(){
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
                .isInstanceOf(ResourceExistsException.class)
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
                1,
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
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(jwtService).generateToken(mapArgumentCaptor.capture(),userArgumentCaptor.capture());
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
                .hasMessage("user not found");
    }

    @Test
    void canChangeUserPassword(){
        //given
        ChangePasswordRequest request = new ChangePasswordRequest(
                "12345",
                "1234"
        );
        Integer userId = 1;
        User mockUser = new User(
                1,
                "lawal Olakunle",
                "larwal@mail.com",
                userService.encodePassword("12345"),
                true);

        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        //when
        userService.changeUserPassword(request,userId);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userService.passwordMatches(request.newPassword(),userArgumentCaptor.getValue().getPassword())).isTrue();
    }

    @Test
    void willThrowValueMismatchExceptionWhenOldPasswordDoesNotMatchWhenChangingPassword(){
        //given
        ChangePasswordRequest request = new ChangePasswordRequest(
                "1245",
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
        given(userRepository.findById(userId)).willReturn(Optional.of(mockUser));

        //when
        //then
        assertThatThrownBy(() ->userService.changeUserPassword(request,userId))
                .isInstanceOf(ValueMismatchException.class)
                .hasMessage("old password does not match");
    }
    @Test
    void willThrowResourceNotFoundExceptionWhenUserNotFoundForUserIdWhenChangingPassword(){
        //given
        ChangePasswordRequest request = new ChangePasswordRequest(
                "1245",
                "1234"
        );
        Integer userId = 1;

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() ->userService.changeUserPassword(request,userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user not found");
    }

}