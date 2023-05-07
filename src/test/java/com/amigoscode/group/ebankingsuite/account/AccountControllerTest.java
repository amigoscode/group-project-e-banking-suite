package com.amigoscode.group.ebankingsuite.account;

import com.amigoscode.group.ebankingsuite.account.request.AccountTransactionPinUpdateModel;
import com.amigoscode.group.ebankingsuite.config.JWTService;
import com.amigoscode.group.ebankingsuite.universal.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private JWTService jwtService;
    private AccountController accountController;

    @BeforeEach
    public void setUp() {
        this.accountService = new AccountService(accountRepository);
        this.accountController = new AccountController(accountService, jwtService);
    }

    @Test
    void willReturn200whenGettingUserAccountOverview() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        Integer userId = 1;
        Account newAccount = new Account(
                1,
                new BigDecimal(0),
                AccountStatus.ACTIVATED,
                "6767576476",
                Tier.LEVEL1,
                "8493"
        );
        String testJwt = "Bearer " + "testToken";

        given(jwtService.extractUserIdFromToken(testJwt)).willReturn(1);
        given(accountRepository.findAccountByUserId(userId)).willReturn(Optional.of(newAccount));

        //when
        ResponseEntity<ApiResponse> responseEntity =
                accountController.getUserAccountOverview(testJwt);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void willReturn404whenAccountNotFoundForUserIdWhenGeneratingAccountOverview() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        Integer userId = 1;
        String testJwt = "Bearer " + "testToken";

        given(jwtService.extractUserIdFromToken(testJwt)).willReturn(1);
        given(accountRepository.findAccountByUserId(userId)).willReturn(Optional.empty());

        //when
        ResponseEntity<ApiResponse> responseEntity =
                accountController.getUserAccountOverview(testJwt);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void willReturn200whenClosingUserAccount() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));


        Integer userId = 1;
        Account newAccount = new Account(
                1,
                new BigDecimal(0),
                AccountStatus.ACTIVATED,
                "6767576476",
                Tier.LEVEL1,
                "8493"
        );
        String testJwt = "Bearer " + "testToken";

        given(jwtService.extractUserIdFromToken(testJwt)).willReturn(1);
        given(accountRepository.findAccountByUserId(userId)).willReturn(Optional.of(newAccount));

        //when
        ResponseEntity<ApiResponse> responseEntity =
                accountController.closeAccount(testJwt);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }



    @Test
    void willReturn200WhenUpdatingTransactionPin() {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        AccountTransactionPinUpdateModel pinUpdateModel =
                new AccountTransactionPinUpdateModel("1234");
        Integer userId = 1;
        Account existingAccount = new Account(
                1,
                new BigDecimal(0),
                AccountStatus.ACTIVATED,
                "6767576476",
                Tier.LEVEL1,
                "8493"
        );
        String testJwt = "Bearer " + "testToken";

        given(jwtService.extractUserIdFromToken(testJwt)).willReturn(1);
        given(accountRepository.findAccountByUserId(userId)).willReturn(Optional.of(existingAccount));

        //when
        ResponseEntity<ApiResponse> responseEntity =
                accountController.updateAccountTransactionPin(testJwt,pinUpdateModel);
        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


}