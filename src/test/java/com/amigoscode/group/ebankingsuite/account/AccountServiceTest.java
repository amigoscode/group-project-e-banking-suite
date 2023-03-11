package com.amigoscode.group.ebankingsuite.account;

import com.amigoscode.group.ebankingsuite.account.response.AccountOverviewResponse;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    private AccountService accountService;


    @BeforeEach
    void setUp() {
        this.accountService = new AccountService(accountRepository);
    }

    @Test
    void canCreateAccount() {
        //given
        Account newAccount = new Account(
                1,
                new BigDecimal(0),
                AccountStatus.ACTIVATED,
                "6767576476",
                Tier.LEVEL1,
                "8493"
        );

        //when
        accountService.createAccount(newAccount);
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        assertThat(accountArgumentCaptor.getValue()).isEqualToIgnoringGivenFields(newAccount,"id");
    }

    @Test
    void canGetAccountByUserIdWhenAccountExists() {
        //given
        Integer userId = 1;
        Account mockUserAccount = new Account(
                1,
                new BigDecimal(0),
                AccountStatus.ACTIVATED,
                "6767576476",
                Tier.LEVEL1,
                "8493"
        );

        given(accountRepository.findAccountByUserId(userId)).willReturn(Optional.of(mockUserAccount));

        //when
        Account userAccount = accountService.getAccountByUserId(userId);
        //then
        assertThat(userAccount).isEqualToIgnoringGivenFields(mockUserAccount,"id");
    }
    @Test
    void willThrowResourceNotFoundExceptionWhenNoAccountFoundForUserId() {
        //given
        Integer userId = 1;

        given(accountRepository.findAccountByUserId(userId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> accountService.getAccountByUserId(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("account not found");
    }

    @Test
    void canGenerateAccountOverviewByUserId() {
        //given
        Integer userId = 1;
        Account newAccount = new Account(
                1,
                new BigDecimal(0),
                AccountStatus.ACTIVATED,
                "6767576476",
                Tier.LEVEL1,
                "8493"
        );
        given(accountRepository.findAccountByUserId(userId)).willReturn(Optional.of(newAccount));

        //when
        AccountOverviewResponse response = accountService.generateAccountOverviewByUserId(userId);
        //then
        assertThat(response).isEqualTo(new AccountOverviewResponse(
                newAccount.getAccountBalance(),
                newAccount.getAccountNumber(),
                newAccount.getTierLevel().name(),
                newAccount.getAccountStatus().name())
        );

    }

//    @Test
//    void updateAccount() {
//        //given
//        Account existingAccount = new Account(
//                1,
//                new BigDecimal(0),
//                AccountStatus.ACTIVATED,
//                "6767576476",
//                Tier.LEVEL1,
//                "8493"
//        );
//
//        //when
//        accountService.createAccount(newAccount);
//        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
//        verify(accountRepository).save(accountArgumentCaptor.capture());
//
//        //then
//        assertThat(accountArgumentCaptor.getValue()).isEqualToIgnoringGivenFields(newAccount,"id");
//    }
}