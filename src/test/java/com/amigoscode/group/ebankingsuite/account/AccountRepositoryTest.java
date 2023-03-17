package com.amigoscode.group.ebankingsuite.account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void deleteDataInTable(){
        accountRepository.deleteAll();
    }

    @Test
    void canFindAccountByUserId() {
        //given
        Integer userId = 1;
        Account testAccount = new Account(
                1,
                new BigDecimal(0),
                AccountStatus.ACTIVATED,
                "12345456",
                Tier.LEVEL1,
                "1234");
        accountRepository.save(testAccount);

        //when
        Optional<Account> account = accountRepository.findAccountByUserId(userId);

        //then
        assertThat(account.get()).isEqualTo(testAccount);
    }
}