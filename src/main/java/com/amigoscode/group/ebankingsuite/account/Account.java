package com.amigoscode.group.ebankingsuite.account;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Account.
 */
@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor

public class Account {

    @Id
    @SequenceGenerator(
            name = "account_id_sequence",
            sequenceName = "account_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_id_sequence"
    )
    private Integer Id;
    private Integer userId;
    private BigDecimal accountBalance;
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    private Tier tierLevel;
    private String transactionPin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Account(Integer userId){
        this.setUserId(userId);
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.setAccountBalance(new BigDecimal(0));
    }
    public Account(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.setAccountBalance(new BigDecimal(0));
    }

    public Account(Integer userId, BigDecimal accountBalance, AccountStatus accountStatus, String accountNumber, Tier tierLevel, String transactionPin) {
        this.userId = userId;
        this.accountBalance = accountBalance;
        this.accountStatus = accountStatus;
        this.accountNumber = accountNumber;
        this.tierLevel = tierLevel;
        this.transactionPin = transactionPin;
    }
}