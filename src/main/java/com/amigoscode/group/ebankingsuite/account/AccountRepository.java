package com.amigoscode.group.ebankingsuite.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This is the interface for the account repository.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByUserId(Integer userId);
    Optional<Account> findAccountByAccountNumber(Integer userId);
    boolean existsByAccountNumber(String accountNumber);

}