package com.amigoscode.group.ebankingsuite.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * This is the interface for the closed account repository.
 */
@Repository
public interface ClosedAccountRepository extends JpaRepository<ClosedAccount, Integer> {
    ClosedAccount findByUserId(Integer userId);
}
