package com.amigoscode.group.ebankingsuite.account;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * The type Closed account.
 */

@Entity
@Table(name =  "closed_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor

/*
  This is the class for the closed account.
 */

public class ClosedAccount {

    @Id
    @SequenceGenerator(
            name = "closed_account_id_sequence",
            sequenceName = "closed_account_id_sequence"
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "closed_account_id_sequence"
    )

    private Integer Id;
    private Integer userId;
    private BigDecimal accountBalance;

    private String accountNumber;
    @Enumerated(EnumType.STRING)

    private Tier tierLevel;

    private LocalDateTime closedAt;

    private String relievingReason;

    public ClosedAccount(Account account, String relievingReason) {
        this.userId = account.getUserId();
        this.accountBalance = account.getAccountBalance();
        this.accountNumber = account.getAccountNumber();
        this.tierLevel = account.getTierLevel();
        this.closedAt = LocalDateTime.now();
        this.relievingReason = relievingReason;

    }

}
