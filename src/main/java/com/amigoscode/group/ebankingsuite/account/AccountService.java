package com.amigoscode.group.ebankingsuite.account;

import com.amigoscode.group.ebankingsuite.account.response.AccountOverviewResponse;
import com.amigoscode.group.ebankingsuite.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final ClosedAccountRepository closedAccountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, ClosedAccountRepository closedAccountRepository) {
        this.accountRepository = accountRepository;
        this.closedAccountRepository = closedAccountRepository;
    }

    public void createAccount(Account account) {
        account.setAccountNumber(generateAccountNumber());
        account.setTierLevel(Tier.LEVEL1);
        account.setAccountStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);
    }

    /**
     * This method generates random 10 digit values and convert to string
     * for use as account number for accounts
     */
    private String generateAccountNumber(){
        Random random = new SecureRandom();
        String accountNumber = "";
        for (int i = 0; i <= 9; i++) {
            accountNumber = accountNumber.
                    concat(String.valueOf(Math.abs(random.nextInt(9))));
        }
        return accountNumber;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountByUserId(Integer userId) {
        Optional<Account> account = accountRepository.findAccountByUserId(userId);
        if(account.isEmpty()){
            throw new ResourceNotFoundException("account not found");
        }
        return account.get();
    }

    public AccountOverviewResponse generateAccountOverviewByUserId(Integer userId){
        Account userAccount = getAccountByUserId(userId);
        return new AccountOverviewResponse(
                userAccount.getAccountBalance(),
                userAccount.getAccountNumber(),
                userAccount.getTierLevel().name(),
                userAccount.getAccountStatus().name()
        );
    }

    public void updateAccount(Account existingAccount) {
        existingAccount.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(existingAccount);
    }

    /**
     * This method closes the account by getting the userId from the JWT and the relieving reason
     * from the request body
     */

    public void closeAccount(Integer userId, String relievingReason){
        Account account = getAccountByUserId(userId);
        account.setAccountStatus(AccountStatus.CLOSED);

        ClosedAccount closedAccount = new ClosedAccount(account, relievingReason);
        closedAccount.setRelievingReason(relievingReason);
        closedAccountRepository.save(closedAccount);
        accountRepository.save(account);
    }

}