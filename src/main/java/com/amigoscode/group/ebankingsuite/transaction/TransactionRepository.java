package com.amigoscode.group.ebankingsuite.transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByReferenceNum(String referenceNumber);

    /**
     * The methods below returns a list of transactions that are in a particular status
     */

    List<Transaction> findAllByStatusAndCreatedAtBetweenAndSenderAccountNumber(TransactionStatus status, LocalDateTime startDate, LocalDateTime endDate, String senderAccountNumber);

    List<Transaction> findAllByStatusAndCreatedAtBetweenAndReceiverAccountNumber(TransactionStatus status, LocalDateTime startDate, LocalDateTime endDate, String receiverAccountNumber);
}

