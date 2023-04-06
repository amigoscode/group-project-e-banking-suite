package com.amigoscode.group.ebankingsuite.transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByReferenceNum(String referenceNumber);
}
