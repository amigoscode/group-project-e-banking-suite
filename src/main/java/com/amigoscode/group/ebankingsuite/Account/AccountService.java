package com.amigoscode.group.ebankingsuite.Account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public List<Account> getAccountsByUserId(Integer userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account updateAccount(Integer accountId, Account account) {
        Account existingAccount = accountRepository.findById(accountId).orElse(null);
        if (existingAccount != null) {
            existingAccount.setUserId(account.getUserId());
            existingAccount.setAccountBalance(account.getAccountBalance());
            existingAccount.setAccountStatus(account.getAccountStatus());
            existingAccount.setTierLevel(account.getTierLevel());
            existingAccount.setDateUpdated(new Date());
            return accountRepository.save(existingAccount);
        }
        return null;
    }

    public void deleteAccount(Integer accountId) {
        accountRepository.deleteById(accountId);
    }

}