package com.amigoscode.group.ebankingsuite.account;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer accountId;
    private Integer userId;
    private BigDecimal accountBalance;
    private String accountStatus;
    private String tierLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId.equals(account.accountId) && userId.equals(account.userId) && accountBalance.equals(account.accountBalance) && accountStatus.equals(account.accountStatus) && tierLevel.equals(account.tierLevel) && createdAt.equals(account.createdAt) && updatedAt.equals(account.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, userId, accountBalance, accountStatus, tierLevel, createdAt, updatedAt);
    }
}