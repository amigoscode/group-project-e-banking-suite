package com.amogoscode.groupe.ebankingsuite.User;

import com.amogoscode.groupe.ebankingsuite.User.resquests.UserAuthenticationRequests;
import com.amogoscode.groupe.ebankingsuite.User.resquests.UserRegistrationRequest;
import com.amogoscode.groupe.ebankingsuite.config.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        //then


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

    void canAuthenticateUserAndGenerateJWTWhenEmailAndPasswordMatch(){
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
        given(userRepository.findByEmailAddress(authenticationRequests.emailAddress())).willReturn(Optional.of(mockUser));

        //when
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);




    }
}