package com.amigoscode.group.ebankingsuite.transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    boolean existsByReferenceNum(String referenceNumber);

    Slice<Transaction> findAllByStatusAndCreatedAtBetweenAndSenderAccountNumberOrReceiverAccountNumber(
            TransactionStatus status,LocalDateTime startDate,LocalDateTime endDate, String senderAccountNumber, String receiverAccountNumber, Pageable pageable
    );

}

