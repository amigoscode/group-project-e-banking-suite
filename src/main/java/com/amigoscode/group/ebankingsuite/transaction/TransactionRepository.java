package com.amigoscode.group.ebankingsuite.transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByReferenceNum(String referenceNumber);

    List<Transaction>findAllByStatusAndCreatedAtBetweenAndSenderAccountNumberOrReceiverAccountNumber(TransactionStatus status, LocalDateTime startDate, LocalDateTime endDate, Integer senderAccountNumber, Integer receiverAccountNumber);
}
