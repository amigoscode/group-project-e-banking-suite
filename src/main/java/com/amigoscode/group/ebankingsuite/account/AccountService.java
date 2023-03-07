package com.amigoscode.group.ebankingsuite.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public Account findAccountById(Integer accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    public Optional<Account> getAccountByUserId(Integer userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account updateAccount(Integer accountId, Account account) {
        Account existingAccount = accountRepository.findById(accountId).orElse(null);
        if (accountRepository.existsById(accountId)) {
            assert existingAccount != null;
            existingAccount.setUserId(account.getUserId());
            existingAccount.setAccountBalance(account.getAccountBalance());
            existingAccount.setAccountStatus(account.getAccountStatus());
            existingAccount.setTierLevel(account.getTierLevel());
            existingAccount.setUpdatedAt(account.getUpdatedAt());
            return accountRepository.save(existingAccount);
        }
        return null;
    }

    public void deleteAccount(Integer accountId) {
        accountRepository.deleteById(accountId);
    }

}