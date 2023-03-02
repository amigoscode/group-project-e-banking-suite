package com.amigoscode.group.ebankingsuite.Account;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer accountId;
    @Column(nullable = false)

    private Integer userId;
    @Column(nullable = false)

    private BigDecimal accountBalance;
    @Column(nullable = false)

    private String accountStatus;
    @Column(nullable = false)


    private String tierLevel;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false, updatable = false)
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated", nullable = false)
    private Date dateUpdated;

    // All Arg Constructor
    public Account(Integer accountId, Integer userId, BigDecimal accountBalance, String accountStatus, String tierLevel, Date dateCreated, Date dateUpdated) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountBalance = accountBalance;
        this.accountStatus = accountStatus;
        this.tierLevel = tierLevel;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId.equals(account.accountId) && userId.equals(account.userId) && accountBalance.equals(account.accountBalance) && accountStatus.equals(account.accountStatus) && tierLevel.equals(account.tierLevel) && dateCreated.equals(account.dateCreated) && dateUpdated.equals(account.dateUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, userId, accountBalance, accountStatus, tierLevel, dateCreated, dateUpdated);
    }
}